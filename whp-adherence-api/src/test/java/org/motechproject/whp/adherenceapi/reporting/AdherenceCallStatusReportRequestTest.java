package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

import static org.junit.Assert.assertEquals;

public class AdherenceCallStatusReportRequestTest {

    @Test
    public void shouldCopyCallId() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getCallId(), callLogRequest.getCallId());
    }

    @Test
    public void shouldCopyCallStatus() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getCallStatus(), callLogRequest.getCallStatus());
    }

    @Test
    public void shouldCopyFlashingCallId() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getFlashingCallId(), callLogRequest.getFlashingCallId());
    }

    @Test
    public void shouldCopyProviderId() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getProviderId(), callLogRequest.getProviderId());
    }

    @Test
    public void shouldCopyDisconnectionType(){
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getDisconnectionType(), callLogRequest.getDisconnectionType());
    }

    @Test
    public void shouldCopyEndTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(new WHPDateTime(callStatusRequest.getEndTime()).dateTime().toDate(), callLogRequest.getEndTime());
    }

    @Test
    public void shouldCopyStartTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(new WHPDateTime(callStatusRequest.getStartTime()).dateTime().toDate(), callLogRequest.getStartTime());
    }

    @Test
    public void shouldCopyAttemptTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(new WHPDateTime(callStatusRequest.getAttemptTime()).dateTime().toDate(), callLogRequest.getAttemptTime());
    }

    @Test
    public void shouldCopyPatientCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getPatientCount(), callLogRequest.getTotalPatients().toString());
    }

    @Test
    public void shouldCopyAdherenceCapturedCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getAdherenceCapturedCount(), callLogRequest.getAdherenceCaptured().toString());
    }

    @Test
    public void shouldCopyAdherenceNotCapturedCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getAdherenceNotCapturedCount(), callLogRequest.getAdherenceNotCaptured().toString());
    }

    @Test
    public void shouldCopyCallAnsweredStatus() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        AdherenceCallLogRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callLogRequest();
        assertEquals(callStatusRequest.getCallAnswered(), callLogRequest.getCallAnswered());
    }

    private AdherenceCallStatusRequest callStatusRequest() {
        AdherenceCallStatusRequest callStatusRequest = new AdherenceCallStatusRequest();
        callStatusRequest.setCallId("callid");
        callStatusRequest.setCallStatus("call_status");
        callStatusRequest.setFlashingCallId("flashing_call_id");
        callStatusRequest.setProviderId("provider_id");
        callStatusRequest.setStartTime("10/12/2012 12:32:35");
        callStatusRequest.setEndTime("21/12/2012 12:32:35");
        callStatusRequest.setPatientCount("3");
        callStatusRequest.setAttemptTime("21/12/2012 12:32:35");
        callStatusRequest.setAdherenceCapturedCount("2");
        callStatusRequest.setDisconnectionType("disconnectionType");
        callStatusRequest.setAdherenceNotCapturedCount("2");
        callStatusRequest.setCallAnswered("YES");
        return callStatusRequest;
    }
}
