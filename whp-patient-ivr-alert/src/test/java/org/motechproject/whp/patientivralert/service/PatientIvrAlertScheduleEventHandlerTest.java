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

        patientIvrAlertScheduleEventHandler.startAlerts();

        HashMap<String, Object> params = new HashMap<>();
        params.put(PatientIvrAlertService.REQUEST_ID, requestId);
        params.put(PatientIvrAlertService.OFFSET, 0);
        MotechEvent event = new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params);
        verify(patientIvrAlertService).alert(event);
    }
}
