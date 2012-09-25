package org.motechproject.whp.container.domain;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContainerRegistrationRequestValidator {

    private ContainerService containerService;
    private ProviderContainerMappingService providerContainerMappingService;
    private SputumTrackingProperties sputumTrackingProperties;
    private ProviderService providerService;

    @Autowired
    public ContainerRegistrationRequestValidator(ContainerService containerService, ProviderService providerService,
                                                 ProviderContainerMappingService providerContainerMappingService, SputumTrackingProperties sputumTrackingProperties) {
        this.containerService = containerService;
        this.providerService = providerService;
        this.providerContainerMappingService = providerContainerMappingService;
        this.sputumTrackingProperties = sputumTrackingProperties;
    }

    public List<String> validate(ContainerRegistrationRequest registrationRequest) {
        String containerId = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();
        String providerId = registrationRequest.getProviderId();

        ArrayList<String> errors = new ArrayList<>();
        int containerIdMaxLength = sputumTrackingProperties.getContainerIdMaxLength();
        if (!StringUtils.isNumeric(containerId) || containerId.length() != containerIdMaxLength) {
            errors.add(String.format("Container Id must be of %s digits in length", containerIdMaxLength));
        }

        if (StringUtils.isBlank(providerId))
            errors.add(String.format("Invalid provider id : %s", providerId));

        if (isProviderExists(providerId)) {
            if (!providerContainerMappingService.isValidContainerForProvider(providerId, containerId))
                errors.add(String.format("Invalid container id : %s", containerId));
        } else
            errors.add(String.format("Provider not registered : %s", providerId));

        if (!SputumTrackingInstance.isValid(instance))
            errors.add(String.format("Invalid instance : %s", instance));

        if (containerService.exists(containerId))
            errors.add("Container Id already exists.");

        return errors;
    }

    private boolean isProviderExists(String providerId) {
        return providerService.findByProviderId(providerId) != null;
    }
}
