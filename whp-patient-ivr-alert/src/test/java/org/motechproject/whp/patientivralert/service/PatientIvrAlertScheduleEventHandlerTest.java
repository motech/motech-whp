package org.motechproject.whp.patientivralert.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest.OFFSET;
import static org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest.REQUEST_ID;

public class PatientIvrAlertScheduleEventHandlerTest {

    PatientIvrAlertScheduleEventHandler patientIvrAlertScheduleEventHandler;

    @Mock
    PatientIvrAlertService patientIvrAlertService;
    @Mock
    UUIDGenerator uuidGenerator;

    @Before
    public void setUp() {
        initMocks(this);
        patientIvrAlertScheduleEventHandler = new PatientIvrAlertScheduleEventHandler(patientIvrAlertService, uuidGenerator);
    }

    @Test
    public void shouldStartSendingPatientAlerts() {
        String requestId = "requestId";
        when(uuidGenerator.uuid()).thenReturn(requestId);
        String messageId = "message";

        HashMap<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, messageId);
        patientIvrAlertScheduleEventHandler.startAlerts(new MotechEvent(EventKeys.PATIENT_IVR_ALERT_EVENT_NAME, eventParams));

        MotechEvent event = expectedMotechEvent(requestId, messageId);
        verify(patientIvrAlertService).alert(event);
    }

    private MotechEvent expectedMotechEvent(String requestId, String messageId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(REQUEST_ID, requestId);
        params.put(OFFSET, 0);
        params.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, messageId);
        return new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params);
    }
}
