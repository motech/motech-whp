package org.motechproject.whp.container.domain;

import org.motechproject.whp.container.InvalidContainerIdException;

public class ContainerId {

    public static final String CONTAINER_ID_PREFIX = "s";
    private final String providerId;
    private final String containerId;

    public ContainerId(String providerId, String containerId) {
        if(containerId == null || containerId.length() != 5){
            throw new InvalidContainerIdException(String.format("ContainerId %s is invalid", containerId));
        }

        this.providerId = providerId;
        this.containerId = containerId;
    }

    public String value() {
        return CONTAINER_ID_PREFIX + providerId + containerId;
    }
}
