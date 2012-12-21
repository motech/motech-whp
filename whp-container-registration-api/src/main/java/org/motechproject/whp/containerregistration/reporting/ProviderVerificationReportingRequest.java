package org.motechproject.whp.containerregistration.reporting;

import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.reports.contract.ProviderVerificationLogRequest;

import static org.motechproject.whp.common.util.WHPDateTime.date;

public class ProviderVerificationReportingRequest {

    private ProviderVerificationLogRequest request;

    public ProviderVerificationReportingRequest(ProviderVerificationRequest verificationRequest, String providerId) {
        request = new ProviderVerificationLogRequest();
        request.setCallId(verificationRequest.getCall_id());
        request.setMobileNumber(verificationRequest.getPhoneNumber());
        request.setTime(date(verificationRequest.getTime()).dateTime());
        request.setProviderId(providerId);
    }

    public ProviderVerificationLogRequest request() {
        return request;
    }
}
