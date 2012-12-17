package org.motechproject.whp.containerregistration.api.verification;

import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.container.domain.ContainerId;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

@Component
public class ValidatorPool {
    private ProviderService providerService;
    private ContainerService containerService;
    private ProviderContainerMappingService mappingService;

    @Autowired
    public ValidatorPool(ProviderService providerService, ContainerService containerService, ProviderContainerMappingService mappingService) {
        this.providerService = providerService;
        this.containerService = containerService;
        this.mappingService = mappingService;
    }

    public ValidatorPool verifyMobileNumber(String phoneNumber, WHPErrors whpErrors) {
        Provider provider = providerService.findByMobileNumber(phoneNumber);
        if (null == provider)
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        return this;
    }

    public ValidatorPool verifyContainerMapping(String phoneNumber, String containerId, WHPErrors whpErrors) {
        Provider provider = providerService.findByMobileNumber(phoneNumber);
        if (null == provider) {
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
            return this;
        }
        if (containerService.exists(new ContainerId(provider.getProviderId(), containerId, ON_BEHALF_OF_PROVIDER).value())) {
            whpErrors.add(new WHPError(WHPErrorCode.CONTAINER_ALREADY_REGISTERED));
        }else if (!mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)) {
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID, "The container Id entered  is invalid"));
        }
        return this;
    }

    public ValidatorPool verifyPhase(String phase, WHPErrors whpErrors) {
        if (!RegistrationInstance.isValidRegistrationInstanceName(phase))
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHASE));
        return this;
    }
}
