package org.motechproject.whp.refdata.seed.version5;

import org.apache.log4j.Logger;
import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientReportSeed {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final PatientService patientService;

    @Autowired
    public PatientReportSeed(PatientService patientService) {
        this.patientService = patientService;
    }

    @Seed(priority = 3, version = "5.0")
    public void migratePatients() {
        List<Patient> patientList = null;

        try{
            patientList = patientService.getAll();
        } catch(Exception e){
            logger.error("Error occurred fetching list of patients. Trying again..");
            patientList = patientService.getAll();
        }

        for (Patient patient : patientList){
            patientService.update(patient);
        }
    }
}
