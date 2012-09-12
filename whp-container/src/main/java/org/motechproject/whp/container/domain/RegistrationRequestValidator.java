package org.motechproject.whp.container.domain;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegistrationRequestValidator {

    private ContainerService containerService;

    @Autowired
    public RegistrationRequestValidator(ContainerService containerService) {
        this.containerService = containerService;
    }

    public List<String> validate(RegistrationRequest registrationRequest) {
        String containerId = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();

        ArrayList<String> errors = new ArrayList<>();
        if(!StringUtils.isNumeric(containerId) || containerId.length() != 10)
            errors.add("Container Id must be of 10 digits in length");

        if(!Instance.isValid(instance))
            errors.add(String.format("Invalid instance : %s", instance));

        if(containerService.exists(containerId))
            errors.add("Container Id already exists.");

        return errors;
    }
}
