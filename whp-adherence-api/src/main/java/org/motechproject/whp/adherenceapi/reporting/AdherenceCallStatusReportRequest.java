package org.motechproject.whp.adherenceapi.reporting;


import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

public class AdherenceCallStatusReportRequest {

    private AdherenceCallStatusRequest callStatusRequest;

    public AdherenceCallStatusReportRequest(AdherenceCallStatusRequest callStatusRequest) {
        this.callStatusRequest = callStatusRequest;
    }

    public AdherenceCallLogRequest callLogRequest() {
        AdherenceCallLogRequest callLogRequest = new AdherenceCallLogRequest();
        callLogRequest.setCalledBy(callStatusRequest.getMsisdn());
        callLogRequest.setCallId(callStatusRequest.getCallId());
        callLogRequest.setCallStatus(callStatusRequest.getCallStatus());
        callLogRequest.setFlashingCallId(callStatusRequest.getFlashingCallId());
        callLogRequest.setProviderId(callStatusRequest.getProviderId());
        callLogRequest.setStartTime(new WHPDateTime(callStatusRequest.getStartTime()).date().toDate());
        callLogRequest.setEndTime(new WHPDateTime(callStatusRequest.getEndTime()).date().toDate());
        callLogRequest.setTotalPatients(Integer.parseInt(callStatusRequest.getPatientCount()));
        callLogRequest.setAdherenceCaptured(Integer.parseInt(callStatusRequest.getAdherenceCapturedCount()));
        return callLogRequest;
    }
}
