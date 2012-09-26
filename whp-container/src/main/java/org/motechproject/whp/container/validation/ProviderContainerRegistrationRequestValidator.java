package org.motechproject.whp.container.validation;

import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderContainerRegistrationRequestValidator {

    private ContainerRegistrationRequestValidator containerRegistrationRequestValidator;
    private ProviderContainerMappingService providerContainerMappingService;

    @Autowired
    public ProviderContainerRegistrationRequestValidator(ContainerRegistrationRequestValidator containerRegistrationRequestValidator, ProviderContainerMappingService providerContainerMappingService) {
        this.containerRegistrationRequestValidator = containerRegistrationRequestValidator;
        this.providerContainerMappingService = providerContainerMappingService;
    }

}
