package org.motechproject.whp.adherenceapi.reporting;


import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

import java.util.Date;

import static java.lang.Integer.parseInt;

public class AdherenceCallStatusReportRequest {

    private AdherenceCallLogRequest callLogRequest;

    public AdherenceCallStatusReportRequest(AdherenceCallStatusRequest callStatusRequest) {
        this.callLogRequest = new AdherenceCallLogRequest();
        setCallDetails(callStatusRequest);
        setProviderDetails(callStatusRequest);
        setTimeDetails(callStatusRequest);
        setAdherenceDetails(callStatusRequest);
    }

    public AdherenceCallLogRequest callLogRequest() {
        return callLogRequest;
    }

    private void setAdherenceDetails(AdherenceCallStatusRequest callStatusRequest) {
        callLogRequest.setTotalPatients(parseInt(callStatusRequest.getPatientCount()));
        callLogRequest.setAdherenceCaptured(parseInt(callStatusRequest.getAdherenceCapturedCount()));
        callLogRequest.setAdherenceNotCaptured(parseInt(callStatusRequest.getAdherenceNotCapturedCount()));
    }

    private void setTimeDetails(AdherenceCallStatusRequest callStatusRequest) {
        callLogRequest.setStartTime(asDate(callStatusRequest.getStartTime()));
        callLogRequest.setEndTime(asDate(callStatusRequest.getEndTime()));
        callLogRequest.setAttemptTime(asDate(callStatusRequest.getAttemptTime()));
    }

    private void setProviderDetails(AdherenceCallStatusRequest callStatusRequest) {
        callLogRequest.setProviderId(callStatusRequest.getProviderId());
    }

    private void setCallDetails(AdherenceCallStatusRequest callStatusRequest) {
        callLogRequest.setCallId(callStatusRequest.getCallId());
        callLogRequest.setCallStatus(callStatusRequest.getCallStatus());
        callLogRequest.setCallAnswered(callStatusRequest.getCallAnswered());
        callLogRequest.setFlashingCallId(callStatusRequest.getFlashingCallId());
    }

    private Date asDate(String startTime) {
        return new WHPDateTime(startTime).asDate();
    }
}
