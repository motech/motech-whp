package org.motechproject.whp.patientreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientReminderEventHandler {
	private final PatientReminderService patientReminderService;

    @Autowired
    public PatientReminderEventHandler(PatientReminderService patientReminderService) {
        this.patientReminderService = patientReminderService;
    }

    @MotechListener(subjects = EventKeys.PATIENT_REMINDER_GOVERNMENT_CATEGORY)
    public void patientReminderEventGovernmentCategory(MotechEvent motechEvent) {
        patientReminderService.alertPatientsUnderGovernmentTreatmentCategory();
    }

    @MotechListener(subjects = EventKeys.PATIENT_REMINDER_COMMERCIAL_CATEGORY)
    public void patientReminderEventCommercialCategory(MotechEvent motechEvent) {
        patientReminderService.alertPatientsUnderCommercialTreatmentCategory();
    }
}
