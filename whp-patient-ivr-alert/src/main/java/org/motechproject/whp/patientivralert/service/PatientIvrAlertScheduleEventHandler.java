package org.motechproject.whp.patientivralert.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.common.event.EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID;

@Service
public class PatientIvrAlertScheduleEventHandler {

    private final PatientIvrAlertService patientIvrAlertService;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public PatientIvrAlertScheduleEventHandler(PatientIvrAlertService patientIvrAlertService, UUIDGenerator uuidGenerator) {
        this.patientIvrAlertService = patientIvrAlertService;
        this.uuidGenerator = uuidGenerator;
    }

    @MotechListener(subjects = EventKeys.PATIENT_IVR_ALERT_EVENT_NAME)
    public void startAlerts(MotechEvent motechEvent) {
        String requestId = uuidGenerator.uuid();
        String messageId = (String) motechEvent.getParameters().get(SCHEDULE_CONFIGURATION_MESSAGE_ID);
        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(requestId, messageId, 0);

        patientIvrAlertService.alert(request.createMotechEvent());
    }
}
