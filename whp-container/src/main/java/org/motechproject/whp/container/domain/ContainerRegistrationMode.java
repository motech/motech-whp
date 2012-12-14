package org.motechproject.whp.container.domain;

import lombok.Getter;

public enum ContainerRegistrationMode{
    ON_BEHALF_OF_PROVIDER(5),
    NEW_CONTAINER(11);

    @Getter
    private int validContainerIdLength;

    ContainerRegistrationMode(int validContainerIdLength) {
        this.validContainerIdLength = validContainerIdLength;
    }
}