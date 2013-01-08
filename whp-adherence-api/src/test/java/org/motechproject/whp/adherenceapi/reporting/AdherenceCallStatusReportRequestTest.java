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
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getCallId(), callLogRequest.getCallId());
    }

    @Test
    public void shouldCopyCallStatus() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getCallStatus(), callLogRequest.getCallStatus());
    }

    @Test
    public void shouldCopyFlashingCallId() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getFlashingCallId(), callLogRequest.getFlashingCallId());
    }

    @Test
    public void shouldCopyProviderId() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getProviderId(), callLogRequest.getProviderId());
    }

    @Test
    public void shouldCopyDisconnectionType(){
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getDisconnectionType(), callLogRequest.getDisconnectionType());
    }

    @Test
    public void shouldCopyEndTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getEndTime(), callLogRequest.getEndTime());
    }

    @Test
    public void shouldCopyStartTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getStartTime(), callLogRequest.getStartTime());
    }

    @Test
    public void shouldCopyAttemptTime() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getAttemptTime(), callLogRequest.getAttemptTime());
    }

    @Test
    public void shouldCopyPatientCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getPatientCount(), callLogRequest.getTotalPatients().toString());
    }

    @Test
    public void shouldCopyAdherenceCapturedCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getAdherenceCapturedCount(), callLogRequest.getAdherenceCaptured().toString());
    }

    @Test
    public void shouldCopyAdherenceNotCapturedCount() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
        assertEquals(callStatusRequest.getAdherenceNotCapturedCount(), callLogRequest.getAdherenceNotCaptured().toString());
    }

    @Test
    public void shouldCopyCallAnsweredStatus() {
        AdherenceCallStatusRequest callStatusRequest = callStatusRequest();
        org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callLogRequest = new AdherenceCallStatusReportRequest(callStatusRequest).callStatusRequest();
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
