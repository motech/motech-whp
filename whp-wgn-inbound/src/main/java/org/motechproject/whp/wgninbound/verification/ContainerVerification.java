package org.motechproject.whp.wgninbound.verification;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContainerVerification extends Verification<ContainerVerificationRequest> {

    private ProviderVerification providerVerification;
    private ProviderService providerService;
    private ProviderContainerMappingService mappingService;
    private ContainerService containerService;

    @Autowired
    public ContainerVerification(RequestValidator validator,
                                 ProviderVerification providerVerification,
                                 ProviderService providerService,
                                 ProviderContainerMappingService mappingService,
                                 ContainerService containerService) {
        super(validator);
        this.providerVerification = providerVerification;
        this.providerService = providerService;
        this.mappingService = mappingService;
        this.containerService = containerService;
    }

    @Override
    protected WHPError verify(ContainerVerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String containerId = request.getContainer_id();
        return verifyContainer(phoneNumber, containerId);
    }

    public WHPError verifyContainer(String phoneNumber, String containerId) {
        WHPError error = providerVerification.verifyMobileNumber(phoneNumber);
        if (null == error) {
            Provider provider = providerService.findByMobileNumber(phoneNumber);
            return verifyContainerMapping(provider, containerId);
        } else {
            return error;
        }
    }

    private WHPError verifyContainerMapping(Provider provider, String containerId) {
        if (containerService.exists(containerId)) {
            return new WHPError(WHPErrorCode.CONTAINER_ALREADY_REGISTERED);
        }else if (!mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)) {
            return new WHPError(WHPErrorCode.INVALID_CONTAINER_ID, "The container Id entered  is invalid");
        }
        return null;
    }
}
