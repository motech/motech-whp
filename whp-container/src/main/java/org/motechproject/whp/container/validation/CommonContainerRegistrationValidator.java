package org.motechproject.whp.container.validation;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerId;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;


@Component
public class CommonContainerRegistrationValidator {

    private ContainerService containerService;
    private SputumTrackingProperties sputumTrackingProperties;
    private ProviderService providerService;

    @Autowired
    public CommonContainerRegistrationValidator(ContainerService containerService, ProviderService providerService,
                                                SputumTrackingProperties sputumTrackingProperties) {
        this.containerService = containerService;
        this.providerService = providerService;
        this.sputumTrackingProperties = sputumTrackingProperties;
    }

    public List<ErrorWithParameters> validate(ContainerRegistrationRequest registrationRequest) {
        String containerIdSequence = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();
        String providerId = registrationRequest.getProviderId();

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
//        int containerIdMaxLength = sputumTrackingProperties.getContainerIdMaxLength();

//        if (!StringUtils.isNumeric(containerIdSequence) || containerIdSequence.length() != containerIdMaxLength) {
//            errors.add(new ErrorWithParameters("container.id.length.error", valueOf(containerIdMaxLength)));
//        }

        if (StringUtils.isBlank(providerId))
            errors.add(new ErrorWithParameters("provider.id.invalid.error", providerId));

        if (!RegistrationInstance.isValidRegistrationInstance(instance))
            errors.add(new ErrorWithParameters("invalid.instance.error", instance));

        if (!isProviderExists(providerId))
            errors.add(new ErrorWithParameters("provider.not.registered.error", providerId));

        ContainerId containerId = new ContainerId(providerId, containerIdSequence, ON_BEHALF_OF_PROVIDER);
        if (containerService.exists(containerId.value()))
            errors.add(new ErrorWithParameters("container.already.registered.error"));

        return errors;
    }

    private boolean isProviderExists(String providerId) {
        return providerService.findByProviderId(providerId) != null;
    }
}
