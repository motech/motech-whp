package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.SputumTrackingRequest;

public class SputumTrackingRequestBuilder {
    private Container container;
    private SputumTrackingRequest containerRegistrationRequest;

    public SputumTrackingRequestBuilder forContainer(Container container) {
        this.container = container;
        containerRegistrationRequest = new SputumTrackingRequest();
        withBasicDetails();
        return this;
    }

    public void withBasicDetails() {
        containerRegistrationRequest.setContainerId(container.getContainerId());
        containerRegistrationRequest.setDateIssuedOn(container.getContainerIssuedDate().toDate());
        containerRegistrationRequest.setInstance(container.getInstance().name());
        containerRegistrationRequest.setContainerStatus(container.getStatus().name());
        containerRegistrationRequest.setProviderId(container.getProviderId());
        containerRegistrationRequest.setLocationId(container.getDistrict());
    }

    public SputumTrackingRequestBuilder registeredThrough(String channelId) {
        containerRegistrationRequest.setChannelId(channelId);
        return this;
    }

    public SputumTrackingRequest build() {
        return containerRegistrationRequest;
    }

    public SputumTrackingRequestBuilder withSubmitterId(String creatorId) {
        containerRegistrationRequest.setSubmitterId(creatorId);
        return this;
    }

    public SputumTrackingRequestBuilder withSubmitterRole(String role) {
        containerRegistrationRequest.setSubmitterRole(role);
        return this;
    }
}
