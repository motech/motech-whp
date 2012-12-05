package org.motechproject.whp.adherenceapi.reporting;


import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.reports.contract.FlashingLogRequest;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.util.WHPDateTime.date;

public class AdherenceFlashingRequest {

    private AdherenceCaptureFlashingRequest flashingRequest;
    private String providerId;

    public AdherenceFlashingRequest(AdherenceCaptureFlashingRequest flashingRequest, String providerId) {
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
