package org.motechproject.whp.container.domain;

import org.motechproject.whp.container.InvalidContainerIdException;

import static org.apache.commons.lang.StringUtils.length;
import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

public class ContainerId {

    public static final String CONTAINER_ID_PREFIX = "s";
    private final String providerId;
    private final String containerId;
    private ContainerRegistrationMode registrationMode;

    public ContainerId(String providerId, String containerId, ContainerRegistrationMode registrationMode) {
        if(length(containerId) != validContainerIdLengthFor(registrationMode)){
            throw new InvalidContainerIdException(String.format("ContainerId %s is invalid", containerId));
        }
        this.providerId = providerId;
        this.containerId = containerId;
        this.registrationMode = registrationMode;
    }

    private int validContainerIdLengthFor(ContainerRegistrationMode registrationMode) {
        if(registrationMode == ON_BEHALF_OF_PROVIDER){
            return 5;
        }
        return 11;
    }

    public String value() {
        if(registrationMode == ON_BEHALF_OF_PROVIDER)
            return CONTAINER_ID_PREFIX + providerId + containerId;
        return containerId;
    }
}
