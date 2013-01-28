package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.adherence.request.DailyAdherenceRequests;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.adherence.criteria.TherapyStartCriteria.shouldStartOrRestartTreatment;
import static org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest;
import static org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequestByProvider;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

@Component
public class TreatmentUpdateOrchestrator {

    private PatientService patientService;
    private WHPAdherenceService whpAdherenceService;
    private ReportingPublisherService reportingPublisherService;

    @Autowired
    public TreatmentUpdateOrchestrator(PatientService patientService, WHPAdherenceService whpAdherenceService, ReportingPublisherService reportingPublisherService) {
        this.patientService = patientService;
        this.whpAdherenceService = whpAdherenceService;
        this.reportingPublisherService = reportingPublisherService;
    }

    public void adjustPhaseStartDates(String patientId, LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        Patient patient = patientService.findByPatientId(patientId);
        patient.adjustPhaseDates(ipStartDate, eipStartDate, cpStartDate);
        recomputePillStatus(patient);
        attemptPhaseTransition(patient);
        patientService.update(patient);
    }

    public void setNextPhase(String patientId, Phase phaseToTransitionTo) {
        Patient patient = patientService.findByPatientId(patientId);
        patient.nextPhaseName(phaseToTransitionTo);

        attemptPhaseTransition(patient);
        patientService.update(patient);
    }

    public void updateDoseInterruptions(Patient patient) {
        Set<LocalDate> adherenceDates = whpAdherenceService.getAdherenceDates(patient);
        patient.updateDoseInterruptions(adherenceDates);
    }

    public void recordWeeklyAdherence(WeeklyAdherenceSummary weeklyAdherenceSummary, String patientId, AuditParams auditParams) {
        Patient patient = patientService.findByPatientId(patientId);

        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }

        whpAdherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);
        refreshPatient(patient, weeklyAdherenceSummary.getWeek().startDate());

        String providerId = patient.getCurrentProviderId();
        AdherenceSubmissionRequest request = createAdherenceSubmissionRequestByProvider(providerId, now());
        reportingPublisherService.reportAdherenceSubmission(request);
    }

    public void recordDailyAdherence(DailyAdherenceRequests dailyAdherenceRequests, Patient patient, AuditParams auditParams) {
        if (dailyAdherenceRequests.isEmpty())
            return;

        whpAdherenceService.recordDailyAdherence(dailyAdherenceRequests, patient, auditParams);
        refreshPatient(patient, dailyAdherenceRequests.getLastAdherenceProvidedWeekStartDate());
        reportingPublisherService.reportAdherenceSubmission(createAdherenceSubmissionRequest(patient.getCurrentProviderId(), auditParams.getUser(), now(), dailyAdherenceRequests.maxDoseDate()));
    }

    public void processAlertsBasedOnConfiguration(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        updateDoseInterruptions(patient);
        patientService.updateBasedOnAlertConfiguration(patient);
    }

    private void recomputePillStatus(Patient patient) {
        updateDoseTakenCount(patient);
        updateDoseInterruptions(patient);
    }

    private void updateDoseTakenCount(Patient patient) {
        Phases phases = patient.getCurrentTherapy().getPhases();
        for (Phase phase : patient.getHistoryOfPhases()) {
            LocalDate startDate = phases.getStartDate(phase);
            LocalDate endDate = phases.getNextPhaseStartDate(phase) != null ? phases.getNextPhaseStartDate(phase).minusDays(1) : DateUtil.today();
            updateDoseTakenCount(patient, phase, startDate, endDate);//Doses taken till Saturday
            endDate = week(endDate).dateOf(DayOfWeek.Sunday);
            updateDoseTakenCount(patient, phase, startDate, endDate);// Doses taken till Sunday
        }
    }

    private void updateDoseTakenCount(Patient patient, Phase phase, LocalDate startDate, LocalDate endDate) {
        int dosesTaken = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), startDate, endDate);
        patient.setNumberOfDosesTaken(phase, dosesTaken, endDate);
    }

    private void attemptPhaseTransition(Patient patient) {
        if (patient.isTransitioning() && patient.getRemainingDosesInLastCompletedPhase() > 0) {
            patient.revertAutoCompleteOfLastPhase();
        }

        if (patient.latestPhaseDoseComplete()) {
            PhaseRecord latestPhaseRecord = patient.latestPhaseRecord();
            AdherenceRecord recordOfLastDoseInPhase = whpAdherenceService.nThTakenDose(patient.getPatientId(), patient.getCurrentTherapy().getUid(),
                    patient.numberOfDosesForPhase(latestPhaseRecord.getName()), latestPhaseRecord.getStartDate());
            patient.endLatestPhase(recordOfLastDoseInPhase.doseDate());
        }

        if (patient.isTransitioning() && !patient.isOrHasBeenOnCp() && patient.hasPhaseToTransitionTo()) {
            patient.startNextPhase();
        }

        recomputePillStatus(patient);

        while (patient.currentPhaseDoseComplete()) {
            attemptPhaseTransition(patient);
        }
    }

    private void refreshPatient(Patient patient, LocalDate lastAdherenceWeekStartDate) {
        patient.setLastAdherenceWeekStartDate(lastAdherenceWeekStartDate);
        recomputePillStatus(patient);
        attemptPhaseTransition(patient);
        patientService.update(patient);
    }
}
