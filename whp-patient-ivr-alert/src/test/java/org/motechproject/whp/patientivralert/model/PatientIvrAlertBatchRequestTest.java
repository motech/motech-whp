package org.motechproject.whp.patientivralert.model;

import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest.*;

public class PatientIvrAlertBatchRequestTest {

    @Test
    public void shouldCreateMotechEvent() {
        String requestId = "requestId";
        String messageId = "messageId";
        int offset = 10;

        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(requestId, messageId, offset);

        MotechEvent motechEvent = request.createMotechEvent();
        assertEquals(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, motechEvent.getSubject());
        assertEquals(requestId, motechEvent.getParameters().get(REQUEST_ID));
        assertEquals(offset, motechEvent.getParameters().get(OFFSET));
        assertEquals(messageId, motechEvent.getParameters().get(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID));
    }

    @Test
    public void shouldPopulateParametersFromMotechEvent() {
        String requestId = "requestId";
        String messageId = "messageId";
        int offset = 10;

        HashMap<String, Object> params = new HashMap<>();
        params.put(REQUEST_ID, requestId);
        params.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, messageId);
        params.put(OFFSET, offset);

        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params));


        assertEquals(requestId, request.getRequestId());
        assertEquals(messageId, request.getMessageId());
        assertEquals(offset, request.getOffset());

    }

    @Test
    public void shouldCreateCallBackEvent() {
        String requestId = "requestId";
        String messageId = "messageId";
        int offset = 10;

        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(requestId, messageId, offset);

        EventCallBack eventCallBack = request.createCallBackEvent();

        MotechEvent callBackEvent = eventCallBack.getCallBackEvent();
        assertEquals(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, callBackEvent.getSubject());
        assertEquals(requestId, callBackEvent.getParameters().get(REQUEST_ID));
        assertEquals(offset, callBackEvent.getParameters().get(OFFSET));
        assertEquals(messageId, callBackEvent.getParameters().get(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID));
    }
}
