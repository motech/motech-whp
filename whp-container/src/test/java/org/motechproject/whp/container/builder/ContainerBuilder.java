package org.motechproject.whp.container.builder;

import org.motechproject.whp.container.domain.Container;

public class ContainerBuilder {

    private Container container = new Container();

    public static ContainerBuilder newContainer() {
        return new ContainerBuilder();
    }

    public ContainerBuilder withDefaults() {
        withProviderId("providerId").withContainerId("containerId");
        return this;
    }

    public ContainerBuilder withProviderId(String providerId) {
        container.setProviderId(providerId);
        return this;
    }

    public ContainerBuilder withContainerId(String containerId) {
        container.setContainerId(containerId);
        return this;
    }

    public ContainerBuilder withPatientId(String patientId) {
        container.setPatientId(patientId);
        return this;
    }

    public Container build() {
        return container;
    }
}
