package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

import static org.junit.Assert.assertEquals;

public class AdherenceCallStatusReportRequestTest {
    @Test
    public void shouldCreateAdherenceCallLogRequest() {
        AdherenceCallStatusRequest callStatusRequest = new AdherenceCallStatusRequest();
        callStatusRequest.setMsisdn("1234567890");
        callStatusRequest.setCallId("callid");
        callStatusRequest.setCallStatus("call_status");
        callStatusRequest.setFlashingCallId("flashing_call_id");
        callStatusRequest.setProviderId("provider_id");
        callStatusRequest.setStartTime("10/12/2012 12:32:35");
        callStatusRequest.setEndTime("21/12/2012 12:32:35");
        callStatusRequest.setPatientCount("3");
        callStatusRequest.setAdherenceCapturedCount("2");

        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();

        assertEquals(callStatusRequest.getMsisdn(), callLogRequest.getCalledBy());
        assertEquals(callStatusRequest.getCallId(), callLogRequest.getCallId());
        assertEquals(callStatusRequest.getCallStatus(), callLogRequest.getCallStatus());
        assertEquals(callStatusRequest.getFlashingCallId(), callLogRequest.getFlashingCallId());
        assertEquals(callStatusRequest.getProviderId(), callLogRequest.getProviderId());
        assertEquals(new WHPDateTime(callStatusRequest.getEndTime()).date().toDate(), callLogRequest.getEndTime());
        assertEquals(new WHPDateTime(callStatusRequest.getStartTime()).date().toDate(), callLogRequest.getStartTime());
        assertEquals(callStatusRequest.getPatientCount(), callLogRequest.getTotalPatients().toString());
        assertEquals(callStatusRequest.getAdherenceCapturedCount(), callLogRequest.getAdherenceCaptured().toString());
    }
}
