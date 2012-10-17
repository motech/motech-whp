package org.motechproject.whp.container.validation;

import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderContainerRegistrationValidator implements ContainerRegistrationValidator<ContainerRegistrationRequest> {

    private CommonContainerRegistrationValidator containerRegistrationRequestValidator;
    private ProviderContainerMappingService providerContainerMappingService;

    @Autowired
    public ProviderContainerRegistrationValidator(CommonContainerRegistrationValidator containerRegistrationRequestValidator, ProviderContainerMappingService providerContainerMappingService) {
        this.containerRegistrationRequestValidator = containerRegistrationRequestValidator;
        this.providerContainerMappingService = providerContainerMappingService;
    }

    public List<ErrorWithParameters> validate(ContainerRegistrationRequest registrationRequest) {
        List<ErrorWithParameters> errors =  this.containerRegistrationRequestValidator.validate(registrationRequest);
        if(!errors.isEmpty()){
            return errors;
        }

        if(!providerContainerMappingService.isValidContainerForProvider(registrationRequest.getProviderId(), registrationRequest.getContainerId())) {
            errors.add(new ErrorWithParameters("container.id.invalid.error", registrationRequest.getContainerId()));
            return errors;
        }

        return new ArrayList<>();
    }
}
