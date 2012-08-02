package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.extract;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

@Component
public class TreatmentUpdateOrchestrator {

    private PatientService patientService;
    private WHPAdherenceService whpAdherenceService;

    @Autowired
    public TreatmentUpdateOrchestrator(PatientService patientService, WHPAdherenceService whpAdherenceService) {
        this.patientService = patientService;
        this.whpAdherenceService = whpAdherenceService;
    }

    public void adjustPhaseStartDates(String patientId, LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        Patient patient = patientService.findByPatientId(patientId);
        patient.adjustPhaseDates(ipStartDate, eipStartDate, cpStartDate);
        patientService.update(patient);
        recomputePillStatus(patient);
        attemptPhaseTransition(patient);
    }

    public void recomputePillStatus(Patient patient) {
        Phases phases = patient.getCurrentTherapy().getPhases();
        for (Phase phase : patient.getHistoryOfPhases()) {
            /*
             1) endDate =  phases.getStartDate(nextPhase) :- will include all doses from start to start (note: Anna Stratis wanted this)
             2) minusDays(1) so that the end of current phase does not overlap with the start of the next phase
            */
            updateTotalDoseTakenCount(patient, phases, phase);
            updateDoseTakenCountTillSunday(patient, phases, phase);
        }
        updateDoseInterruptions(patient);
    }

    public void setNextPhase(String patientId, Phase phaseToTransitionTo) {
        Patient patient = patientService.setNextPhaseName(patientId, phaseToTransitionTo);
        attemptPhaseTransition(patient);
    }

    public void attemptPhaseTransition(Patient patient) {
        if (patient.isTransitioning() && patient.getRemainingDosesInLastCompletedPhase() > 0) {
            patientService.revertAutoCompleteOfLastPhase(patient);
        }

        if (patient.latestPhaseDoseComplete()) {
            PhaseRecord latestPhaseRecord = patient.latestPhaseRecord();
            AdherenceRecord recordOfLastDoseInPhase = whpAdherenceService.nThTakenDose(patient.getPatientId(), patient.getCurrentTherapy().getUid(),
                    patient.numberOfDosesForPhase(latestPhaseRecord.getName()), latestPhaseRecord.getStartDate());
            patientService.autoCompleteLatestPhase(patient, recordOfLastDoseInPhase.doseDate());
        }

        if (patient.isTransitioning() && !patient.isOrHasBeenOnCp() && patient.hasPhaseToTransitionTo()) {
            patientService.startNextPhase(patient);
        }

        recomputePillStatus(patient);

        while (patient.currentPhaseDoseComplete()) {
            attemptPhaseTransition(patient);
        }
    }

    public void updateDoseInterruptions(Patient patient) {
        HashMap<LocalDate, PillStatus> dateAdherenceMap = whpAdherenceService.getDateAdherenceMap(patient);
        List<LocalDate> allDoseDates = patient.getDoseDatesTill(today());

        patientService.clearDoseInterruptionsForUpdate(patient);

        for (LocalDate doseDate : allDoseDates) {
            if (dateAdherenceMap.get(doseDate) == null || dateAdherenceMap.get(doseDate).equals(PillStatus.NotTaken)) {
                patientService.dosesMissedSince(patient, doseDate);
            } else {
                patientService.dosesResumedOnAfterBeingInterrupted(patient, doseDate);
            }
        }
    }

    private void updateTotalDoseTakenCount(Patient patient, Phases phases, Phase phase) {
        LocalDate endDate = phases.getNextPhaseStartDate(phase) != null ? phases.getNextPhaseStartDate(phase).minusDays(1) : DateUtil.today();
        int dosesTaken = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), phases.getStartDate(phase), endDate);
        patientService.updatePillTakenCount(patient, phase, dosesTaken, endDate);
    }

    private void updateDoseTakenCountTillSunday(Patient patient, Phases phases, Phase phase) {
        LocalDate endDate = phases.getNextPhaseStartDate(phase) != null ? phases.getNextPhaseStartDate(phase).minusDays(1) : DateUtil.today();
        LocalDate sundayBeforeEndDate = week(endDate).dateOf(DayOfWeek.Sunday);
        int dosesTakenAsOfLastSunday = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), phases.getStartDate(phase), sundayBeforeEndDate);
        patientService.updatePillTakenCount(patient, phase, dosesTakenAsOfLastSunday, sundayBeforeEndDate);
    }

    public void recordWeeklyAdherence(String patientId,WeeklyAdherenceSummary weeklyAdherenceSummary, AuditParams auditParams) {
        whpAdherenceService.recordWeeklyAdherence(weeklyAdherenceSummary, auditParams);
        Patient patient = patientService.findByPatientId(patientId);
        refreshPatient(patient, weeklyAdherenceSummary.getWeek().startDate());
    }


    public void recordDailyAdherence(List<DailyAdherenceRequest> dailyAdherenceRequests, Patient patient, AuditParams auditParams) {
        whpAdherenceService.recordDailyAdherence(dailyAdherenceRequests,patient,auditParams);
        refreshPatient(patient, getLastAdherenceProvidedWeekStartDate(dailyAdherenceRequests));
    }

    private LocalDate getLastAdherenceProvidedWeekStartDate(List<DailyAdherenceRequest> dailyAdherenceRequests) {
        List<LocalDate> doseDates = extract(dailyAdherenceRequests, on(DailyAdherenceRequest.class).getDoseDate());
        Collections.sort(doseDates);
        LocalDate lastAdherenceProvidedDate =  doseDates.get(doseDates.size() - 1);
        return new TreatmentWeek(lastAdherenceProvidedDate).startDate();
    }

    private void refreshPatient(Patient patient, LocalDate lastAdherenceWeekStartDate) {
        patient.setLastAdherenceWeekStartDate(lastAdherenceWeekStartDate);
        patientService.update(patient);

        recomputePillStatus(patient);
        attemptPhaseTransition(patient);
    }
}
