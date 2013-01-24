package org.motechproject.whp.patientalerts.service;

import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientAlertService {
    private final PatientService patientService;
    private final TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    private AllAlertProcessors allAlertProcessors;

    @Autowired
    public PatientAlertService(PatientService patientService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, AllAlertProcessors allAlertProcessors) {
        this.patientService = patientService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.allAlertProcessors = allAlertProcessors;
    }

    public void updatePatientAlerts(String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        treatmentUpdateOrchestrator.updateDoseInterruptions(patient);
        allAlertProcessors.process(patient);
        patientService.update(patient);
    }
}
