package org.motechproject.whp.containerregistration.service;

import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRContainerRegistrationService {

    private final ProviderVerification providerVerification;
    private final ContainerVerification containerVerification;
    private final ContainerRegistrationVerification containerRegistrationVerification;

    @Autowired
    public IVRContainerRegistrationService(ProviderVerification providerVerification, ContainerVerification containerVerification, ContainerRegistrationVerification containerRegistrationVerification) {
        this.providerVerification = providerVerification;
        this.containerVerification = containerVerification;
        this.containerRegistrationVerification = containerRegistrationVerification;
    }

    public VerificationResult verifyProviderVerificationRequest(ProviderVerificationRequest providerVerificationRequest) {
        VerificationResult verificationResult = providerVerification.verifyRequest(providerVerificationRequest);
        return verificationResult;
    }

    public VerificationResult verifyContainerVerificationRequest(ContainerVerificationRequest request) {
        VerificationResult verificationResult = containerVerification.verifyRequest(request);
        return verificationResult;
    }

    public VerificationResult verifyContainerRegistrationVerificationRequest(IvrContainerRegistrationRequest request) {
        VerificationResult verificationResult = containerRegistrationVerification.verifyRequest(request);
        return verificationResult;
    }
}
