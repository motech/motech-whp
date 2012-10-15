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
    protected WHPError verify(ProviderVerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        return verifyMobileNumber(phoneNumber);
    }

    public WHPError verifyMobileNumber(String phoneNumber) {
        Provider provider = providerService.findByMobileNumber(phoneNumber);
        if (null == provider) {
            return new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER);
        } else {
            return null;
        }
    }
}
