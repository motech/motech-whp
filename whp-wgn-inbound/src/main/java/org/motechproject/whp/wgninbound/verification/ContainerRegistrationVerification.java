package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.wgninbound.request.ValidatorPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContainerRegistrationVerification extends Verification<IvrContainerRegistrationRequest> {

    @Autowired
    public ContainerRegistrationVerification(RequestValidator validator, ValidatorPool validatorPool) {
        super(validator, validatorPool);

    }

    @Override
    protected List<WHPError> verify(IvrContainerRegistrationRequest request) {
        ArrayList<WHPError> whpErrors = new ArrayList<>();

        validatorPool.verifyMobileNumber(request.getPhoneNumber(), whpErrors)
                .verifyContainerMapping(request.getPhoneNumber(), request.getContainer_id(), whpErrors)
                .verifyPhase(request.getPhase(), whpErrors);
        return whpErrors;
    }
}
