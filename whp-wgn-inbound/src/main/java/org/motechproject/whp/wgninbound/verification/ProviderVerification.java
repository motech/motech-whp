package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.request.ValidatorPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderVerification extends Verification<ProviderVerificationRequest> {

    @Autowired
    public ProviderVerification(RequestValidator validator, ValidatorPool validatorPool) {
        super(validator, validatorPool);
    }

    @Override
    protected WHPErrors verify(ProviderVerificationRequest request) {
        WHPErrors whpErrors = new WHPErrors();
        validatorPool.verifyMobileNumber(request.getPhoneNumber(), whpErrors);
        return whpErrors;
    }
}
