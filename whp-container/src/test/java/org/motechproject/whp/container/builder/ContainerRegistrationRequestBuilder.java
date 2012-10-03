package org.motechproject.whp.container.builder;

import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

public class ContainerRegistrationRequestBuilder {

    private ContainerRegistrationRequest request = new ContainerRegistrationRequest();

    public static ContainerRegistrationRequestBuilder newRegistrationRequest() {
        return new ContainerRegistrationRequestBuilder();
    }

    public ContainerRegistrationRequestBuilder withDefaults() {
        withProviderId("providerId")
                .withContainerId("containerId")
                .withInstance(SputumTrackingInstance.PreTreatment);
        return this;
    }

    public ContainerRegistrationRequestBuilder withProviderId(String providerId) {
        request.setProviderId(providerId);
        return this;
    }

    public ContainerRegistrationRequestBuilder withContainerId(String containerId) {
        request.setContainerId(containerId);
        return this;
    }

    public ContainerRegistrationRequestBuilder withInstance(SputumTrackingInstance instance) {
        request.setInstance(instance.getDisplayText());
        return this;
    }

    public ContainerRegistrationRequest build() {
        return request;
    }

}
