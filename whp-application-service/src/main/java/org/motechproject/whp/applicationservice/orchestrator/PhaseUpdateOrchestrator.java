package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phase;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PhaseName;
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
        attemptPhaseTransition(patientId);
    }

    public void recomputePillCount(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        for (Phase phase : patient.getCurrentTherapy().getPhases()) {
            if (phase.hasStarted()) {
                LocalDate endDate = phase.getEndDate() != null ? phase.getEndDate() : DateUtil.today();
                int dosesTaken = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), phase.getStartDate(), endDate);
                patientService.updatePillTakenCount(patient, phase.getName(), dosesTaken);
            }
        }
    }

    public void setNextPhase(String patientId, PhaseName phaseToTransitionTo) {
        patientService.setNextPhaseName(patientId, phaseToTransitionTo);
        attemptPhaseTransition(patientId);
    }

    public void attemptPhaseTransition(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);

        Therapy currentTherapy = patient.getCurrentTherapy();
        if (currentTherapy.currentPhaseDoseComplete()) {
            AdherenceRecord recordOfLastDoseInPhase = whpAdherenceService.nThTakenDose(patientId, currentTherapy.getUid(), currentTherapy.cumulativeNumberOfDosesSoFar(), currentTherapy.getStartDate());
            patientService.endCurrentPhase(patientId, recordOfLastDoseInPhase.doseDate());
            patient = allPatients.findByPatientId(patientId);
        }

        if (patient.isTransitioning() && patient.hasPhaseToTransitionTo()) {
            patientService.startNextPhase(patientId);
        }

        recomputePillCount(patientId);
    }
}
