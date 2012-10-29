package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.SputumTrackingRequest;

public class SputumTrackingRequestBuilder {
    private Container container;
    private String channelId;

    public SputumTrackingRequestBuilder forContainer(Container container) {
        this.container = container;
        return this;
    }

    public SputumTrackingRequestBuilder registeredThrough(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public SputumTrackingRequest build() {
        SputumTrackingRequest containerRegistrationRequest = new SputumTrackingRequest();
        containerRegistrationRequest.setContainerId(container.getContainerId());
        containerRegistrationRequest.setDateIssuedOn(container.getContainerIssuedDate().toDate());
        containerRegistrationRequest.setInstance(container.getInstance().name());
        containerRegistrationRequest.setChannelId(channelId);
        containerRegistrationRequest.setContainerStatus(container.getStatus().name());
        containerRegistrationRequest.setProviderId(container.getProviderId());
        containerRegistrationRequest.setLocationId(container.getDistrict());
        return containerRegistrationRequest;
    }
}
