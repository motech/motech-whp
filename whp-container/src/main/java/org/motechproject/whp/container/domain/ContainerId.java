package org.motechproject.whp.container.domain;

import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

public class ContainerId {

    public static final String CONTAINER_ID_PREFIX = "s";
    private final String providerId;
    private final String containerId;
    private ContainerRegistrationMode registrationMode;

    public ContainerId(String providerId, String containerId, ContainerRegistrationMode registrationMode) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.registrationMode = registrationMode;
    }


    public String value() {
        if(registrationMode == ON_BEHALF_OF_PROVIDER)
            return CONTAINER_ID_PREFIX + providerId + containerId;
        return containerId;
    }
}
