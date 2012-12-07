package org.motechproject.whp.adherenceapi.reporting;


import org.motechproject.whp.reports.contract.FlashingLogRequest;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.util.WHPDateTime.date;

public class AdherenceFlashingReportRequest {

    private org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest flashingRequest;
    private String providerId;

    public AdherenceFlashingReportRequest(org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest flashingRequest, String providerId) {
        this.flashingRequest = flashingRequest;
        this.providerId = providerId;
    }

    public FlashingLogRequest flashingLogRequest() {
        FlashingLogRequest flashingLogRequest = new FlashingLogRequest();
        flashingLogRequest.setCreationTime(now().toDate());
        flashingLogRequest.setProviderId(providerId);
        flashingLogRequest.setCallTime(date(flashingRequest.getCallTime()).date().toDate());
        flashingLogRequest.setFlashingCallId(flashingRequest.getCallId());
        flashingLogRequest.setMobileNumber(flashingRequest.getMsisdn());
        return flashingLogRequest;
    }
}
