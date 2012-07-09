package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhaseUpdateOrchestrator {

    private AllPatients allPatients;
    private PatientService patientService;
    private WHPAdherenceService whpAdherenceService;

    @Autowired
    public PhaseUpdateOrchestrator(AllPatients allPatients, PatientService patientService, WHPAdherenceService whpAdherenceService) {
        this.allPatients = allPatients;
        this.patientService = patientService;
        this.whpAdherenceService = whpAdherenceService;
    }

    public void adjustPhaseStartDates(String patientId, LocalDate ipStartDate, LocalDate eipStartDate, LocalDate cpStartDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.adjustPhaseDates(ipStartDate, eipStartDate, cpStartDate);
        allPatients.update(patient);
        recomputePillCount(patientId);
        attemptPhaseTransition(patientId);
    }

    public void recomputePillCount(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        Phases phases = patient.getCurrentTherapy().getPhases();
        for (Phase phase : patient.getHistoryOfPhases()) {
            LocalDate endDate = phases.getEndDate(phase) != null ? phases.getEndDate(phase) : DateUtil.today();
            int dosesTaken = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), phases.getStartDate(phase), endDate);
            patientService.updatePillTakenCount(patient, phase, dosesTaken);
        }
    }

    public void setNextPhase(String patientId, Phase phaseToTransitionTo) {
        patientService.setNextPhaseName(patientId, phaseToTransitionTo);
        attemptPhaseTransition(patientId);
    }

    public void attemptPhaseTransition(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);

        if (patient.isTransitioning() && patient.getRemainingDosesInLastCompletedPhase() > 0) {
            patientService.revertAutoCompleteOfLastPhase(patient);
        }

        if (patient.currentPhaseDoseComplete()) {
            AdherenceRecord recordOfLastDoseInPhase = whpAdherenceService.nThTakenDose(patientId, patient.getCurrentTherapy().getUid(),
                    patient.numberOfDosesForPhase(patient.getCurrentPhase().getName()), patient.getCurrentPhase().getStartDate());
            patientService.autoCompleteCurrentPhase(patient, recordOfLastDoseInPhase.doseDate());
        }

        if (patient.isTransitioning() && patient.hasPhaseToTransitionTo()) {
            patientService.startNextPhase(patient);
        }

        recomputePillCount(patientId);
    }
}
