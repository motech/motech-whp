package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.reporting.PatientReportingService;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientReportSeed {

    private final PatientService patientService;
    private final PatientReportingService patientReportingService;

    @Autowired
    public PatientReportSeed(PatientService patientService, PatientReportingService patientReportingService) {
        this.patientService = patientService;
        this.patientReportingService = patientReportingService;
    }

    @Seed(priority = 0, version = "5.0")
    public void migratePatients() {
        List<Patient> patientList = patientService.getAll();
        for (Patient patient : patientList){
            patientReportingService.reportPatient(patient);
        }
    }
}
