package org.motechproject.whp.wgninbound.request;

import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public ValidatorPool verifyMobileNumber(String phoneNumber, List<WHPError> whpErrors) {
        Provider provider = providerService.findByMobileNumber(phoneNumber);
        if (null == provider)
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        return this;
    }

    public ValidatorPool verifyContainerMapping(String phoneNumber, String containerId, List<WHPError> whpErrors) {
        Provider provider = providerService.findByMobileNumber(phoneNumber);
        if (null == provider) {
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
            return this;
        }

        if (containerService.exists(containerId)) {
            whpErrors.add(new WHPError(WHPErrorCode.CONTAINER_ALREADY_REGISTERED));
        }else if (!mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)) {
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID, "The container Id entered  is invalid"));
        }
        return this;
    }

    public ValidatorPool verifyPhase(String phase, List<WHPError> whpErrors) {
        if (!SputumTrackingInstance.isValidRegistrationInstance(phase))
            whpErrors.add(new WHPError(WHPErrorCode.INVALID_PHASE));
        return this;
    }
}
