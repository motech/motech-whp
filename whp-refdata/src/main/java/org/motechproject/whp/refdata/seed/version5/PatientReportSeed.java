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
    public void migrateAllPatients() {
        int pageNumber = 1;
        int pageSize = 10;

        List<Patient> patientList;
        do{
            patientList = patientService.getAll(pageNumber, pageSize);
            migratePatients(patientList);
            pageNumber++;
        } while (!patientList.isEmpty());

    }

    private void migratePatients(List<Patient> patientList) {
        for (Patient patient : patientList){
            patientService.update(patient);
        }
    }
}
