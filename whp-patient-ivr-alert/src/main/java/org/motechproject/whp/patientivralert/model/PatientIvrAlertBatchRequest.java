package org.motechproject.whp.patientivralert.model;

import lombok.Getter;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PatientIvrAlertBatchRequest {

    public static final String OFFSET = "offset";
    public static final String REQUEST_ID = "requestId";

    private String requestId;
    private int offset;
    private String messageId;

    public PatientIvrAlertBatchRequest(String requestId, String messageId, int offset) {
        this.requestId = requestId;
        this.offset = offset;
        this.messageId = messageId;
    }

    public PatientIvrAlertBatchRequest(MotechEvent motechEvent) {
        this.requestId = (String) motechEvent.getParameters().get(REQUEST_ID);
        this.messageId = (String) motechEvent.getParameters().get(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID);
        this.offset =  (Integer) motechEvent.getParameters().get(OFFSET);
    }


    public MotechEvent createMotechEvent() {
        Map<String, Object> params = createEventParameters();
        return new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params);
    }

    public EventCallBack createCallBackEvent() {
        HashMap<String, Object> eventParameters = createEventParameters();
        return new EventCallBack(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, eventParameters);
    }

    private HashMap<String, Object> createEventParameters() {
        HashMap<String, Object> params = new HashMap<>();
        params.put(REQUEST_ID, requestId);
        params.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, messageId);
        params.put(OFFSET, offset);
        return params;
    }
}
