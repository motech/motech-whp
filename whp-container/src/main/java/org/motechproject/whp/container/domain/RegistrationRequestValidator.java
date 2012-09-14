package org.motechproject.whp.container.domain;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegistrationRequestValidator {

    private ContainerService containerService;
    private SputumTrackingProperties sputumTrackingProperties;

    @Autowired
    public RegistrationRequestValidator(ContainerService containerService, SputumTrackingProperties sputumTrackingProperties) {
        this.containerService = containerService;
        this.sputumTrackingProperties = sputumTrackingProperties;
    }

    public List<String> validate(RegistrationRequest registrationRequest) {
        String containerId = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();
        String providerId = registrationRequest.getProviderId();

        ArrayList<String> errors = new ArrayList<>();
        int containerIdMaxLength = sputumTrackingProperties.getContainerIdMaxLength();
        if(!StringUtils.isNumeric(containerId) || containerId.length() != containerIdMaxLength) {
            errors.add(String.format("Container Id must be of %s digits in length", containerIdMaxLength));
        }

        if(StringUtils.isBlank(providerId))
            errors.add(String.format("Invalid provider id : %s", providerId));

        if(!Instance.isValid(instance))
            errors.add(String.format("Invalid instance : %s", instance));

        if(containerService.exists(containerId))
            errors.add("Container Id already exists.");

        return errors;
    }
}
