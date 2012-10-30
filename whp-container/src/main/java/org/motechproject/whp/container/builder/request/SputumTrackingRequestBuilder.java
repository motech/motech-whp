package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;

public class SputumTrackingRequestBuilder {
    private Container container;
    private ContainerRegistrationReportingRequest containerRegistrationReportingRequest;

    public SputumTrackingRequestBuilder forContainer(Container container) {
        this.container = container;
        containerRegistrationReportingRequest = new ContainerRegistrationReportingRequest();
        withBasicDetails();
        return this;
    }

    public void withBasicDetails() {
        containerRegistrationReportingRequest.setContainerId(container.getContainerId());
        containerRegistrationReportingRequest.setDateIssuedOn(container.getContainerIssuedDate().toDate());
        containerRegistrationReportingRequest.setInstance(container.getInstance().name());
        containerRegistrationReportingRequest.setContainerStatus(container.getStatus().name());
        containerRegistrationReportingRequest.setProviderId(container.getProviderId());
        containerRegistrationReportingRequest.setLocationId(container.getDistrict());
    }

    public SputumTrackingRequestBuilder registeredThrough(String channelId) {
        containerRegistrationReportingRequest.setChannelId(channelId);
        return this;
    }

    public ContainerRegistrationReportingRequest build() {
        return containerRegistrationReportingRequest;
    }

    public SputumTrackingRequestBuilder withSubmitterId(String creatorId) {
        containerRegistrationReportingRequest.setSubmitterId(creatorId);
        return this;
    }

    public SputumTrackingRequestBuilder withSubmitterRole(String role) {
        containerRegistrationReportingRequest.setSubmitterRole(role);
        return this;
    }
}
