package org.motechproject.whp.container.domain;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.common.error.ErrorWithParameters;
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

import static java.lang.String.valueOf;


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

    public List<ErrorWithParameters> validate(ContainerRegistrationRequest registrationRequest) {
        String containerId = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();
        String providerId = registrationRequest.getProviderId();

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        int containerIdMaxLength = sputumTrackingProperties.getContainerIdMaxLength();
        if (!StringUtils.isNumeric(containerId) || containerId.length() != containerIdMaxLength) {
            errors.add(new ErrorWithParameters("container.id.length.error", valueOf(containerIdMaxLength)));
        }

        if (StringUtils.isBlank(providerId))
            errors.add(new ErrorWithParameters("provider.id.invalid.error", providerId));

        if (isProviderExists(providerId)) {
            if (!providerContainerMappingService.isValidContainerForProvider(providerId, containerId))
                errors.add(new ErrorWithParameters("container.id.invalid.error", containerId));
        } else
            errors.add(new ErrorWithParameters("provider.not.registered.error", providerId));

        if (!SputumTrackingInstance.isValid(instance))
            errors.add(new ErrorWithParameters("invalid.instance.error", instance));

        if (containerService.exists(containerId))
            errors.add(new ErrorWithParameters("container.already.registered.error"));

        return errors;
    }

    private boolean isProviderExists(String providerId) {
        return providerService.findByProviderId(providerId) != null;
    }
}
