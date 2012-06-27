package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phase;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
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

    public void recomputePillCount(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        for (Phase phase : patient.currentTherapy().getPhases()) {
            if (phase.hasStarted()) {
                LocalDate endDate = phase.getEndDate() != null ? phase.getEndDate() : DateUtil.today();
                int dosesTaken = whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), patient.currentTherapyId(), phase.getStartDate(), endDate);
                patientService.updatePillTakenCount(patient, phase.getName(), dosesTaken);
            }
        }
    }

}
