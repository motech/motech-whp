package org.motechproject.whp.refdata.seed.version5;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAlertScheduleSeed {

    private PatientAlertScheduler patientAlertScheduler;
    private PatientService patientService;

    @Autowired
    public PatientAlertScheduleSeed(PatientAlertScheduler patientAlertScheduler, PatientService patientService) {
        this.patientAlertScheduler = patientAlertScheduler;
        this.patientService = patientService;
    }

    @Seed(priority = 2, version = "5.0")
    public void scheduleActivePatients() {
        List<String> allActivePatientIds = patientService.getAllActivePatientIds();

        for (String patientId : allActivePatientIds){
            patientAlertScheduler.scheduleJob(patientId);
        }

    }
}
