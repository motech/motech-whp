package org.motechproject.whp.refdata.seed.version5;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientReportSeed {

    private final PatientService patientService;

    @Autowired
    public PatientReportSeed(PatientService patientService) {
        this.patientService = patientService;
    }

    @Seed(priority = 3, version = "5.0")
    public void migratePatients() {
        List<Patient> patientList = patientService.getAll();
        for (Patient patient : patientList){
            patientService.update(patient);
        }
    }
}
