package org.motechproject.whp.patientivralert.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static org.motechproject.whp.common.event.EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME;
import static org.motechproject.whp.patientivralert.service.PatientIvrAlertService.OFFSET;
import static org.motechproject.whp.patientivralert.service.PatientIvrAlertService.REQUEST_ID;

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
        HashMap<String, Object> params = new HashMap<>();
        params.put(REQUEST_ID, requestId);
        params.put(OFFSET, 0);

        MotechEvent event = new MotechEvent(PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params);
        patientIvrAlertService.alert(event);
    }
}
