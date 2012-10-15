package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderVerification extends Verification<ProviderVerificationRequest> {

    private ProviderService providerService;

    @Autowired
    public ProviderVerification(RequestValidator validator, ProviderService providerService) {
        super(validator);
        this.providerService = providerService;
    }

    @Override
    protected String getVerifiedValue(ProviderVerificationRequest request) {
        return request.getMsisdn();
    }

    @Override
    protected WHPError verify(ProviderVerificationRequest request) {
        Provider provider = providerService.findByMobileNumber(request.getPhoneNumber());
        if (null == provider) {
            return new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER);
        } else {
            return null;
        }
    }
}
