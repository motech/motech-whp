package org.motechproject.whp.adherenceapi.reporting;


import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;

public class AdherenceCallStatusReportRequest {

    private org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callStatusRequest;

    public AdherenceCallStatusReportRequest(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest = new org.motechproject.whp.reports.contract.AdherenceCallStatusRequest();
        setCallDetails(callStatusRequest);
        setProviderDetails(callStatusRequest);
        setTimeDetails(callStatusRequest);
        setAdherenceDetails(callStatusRequest);
    }

    public org.motechproject.whp.reports.contract.AdherenceCallStatusRequest callStatusRequest() {
        return callStatusRequest;
    }

    private void setAdherenceDetails(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest.setTotalPatients(callStatusRequest.getPatientCount());
        this.callStatusRequest.setAdherenceCaptured(callStatusRequest.getAdherenceCapturedCount());
        this.callStatusRequest.setAdherenceNotCaptured(callStatusRequest.getAdherenceNotCapturedCount());
    }

    private void setTimeDetails(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest.setStartTime(callStatusRequest.getStartTime());
        this.callStatusRequest.setEndTime(callStatusRequest.getEndTime());
        this.callStatusRequest.setAttemptTime(callStatusRequest.getAttemptTime());
    }

    private void setProviderDetails(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest.setProviderId(callStatusRequest.getProviderId());
    }

    private void setCallDetails(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest.setDisconnectionType(callStatusRequest.getDisconnectionType());
        this.callStatusRequest.setCallId(callStatusRequest.getCallId());
        this.callStatusRequest.setCallStatus(callStatusRequest.getCallStatus());
        this.callStatusRequest.setCallAnswered(callStatusRequest.getCallAnswered());
        this.callStatusRequest.setFlashingCallId(callStatusRequest.getFlashingCallId());
    }
}
