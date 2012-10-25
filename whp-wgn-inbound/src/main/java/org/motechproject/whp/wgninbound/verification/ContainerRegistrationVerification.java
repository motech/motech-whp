package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContainerRegistrationVerification extends Verification<IvrContainerRegistrationRequest> {

    @Autowired
    public ContainerRegistrationVerification(RequestValidator validator, ValidatorPool validatorPool) {
        super(validator, validatorPool);

    }

    @Override
    public WHPErrors verify(IvrContainerRegistrationRequest request) {
        WHPErrors whpErrors = new WHPErrors();

        validatorPool.verifyMobileNumber(request.getPhoneNumber(), whpErrors)
                .verifyContainerMapping(request.getPhoneNumber(), request.getContainer_id(), whpErrors)
                .verifyPhase(request.getPhase(), whpErrors);
        return whpErrors;
    }
}
