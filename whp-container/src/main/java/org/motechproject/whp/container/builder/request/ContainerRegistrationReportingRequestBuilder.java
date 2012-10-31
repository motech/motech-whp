package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;

public class ContainerRegistrationReportingRequestBuilder {
    private Container container;
    private ContainerRegistrationReportingRequest containerRegistrationReportingRequest;

    public ContainerRegistrationReportingRequestBuilder forContainer(Container container) {
        this.container = container;
        containerRegistrationReportingRequest = new ContainerRegistrationReportingRequest();
        withBasicDetails();
        return this;
    }

    public void withBasicDetails() {
        containerRegistrationReportingRequest.setContainerId(container.getContainerId());
        containerRegistrationReportingRequest.setIssuedOn(container.getContainerIssuedDate().toDate());
        containerRegistrationReportingRequest.setInstance(container.getInstance().name());
        containerRegistrationReportingRequest.setStatus(container.getStatus().name());
        containerRegistrationReportingRequest.setDiagnosis(container.getDiagnosis().name());
        containerRegistrationReportingRequest.setProviderId(container.getProviderId());
        containerRegistrationReportingRequest.setLocationId(container.getDistrict());
    }

    public ContainerRegistrationReportingRequestBuilder registeredThrough(String channelId) {
        containerRegistrationReportingRequest.setChannelId(channelId);
        return this;
    }

    public ContainerRegistrationReportingRequest build() {
        return containerRegistrationReportingRequest;
    }

    public ContainerRegistrationReportingRequestBuilder withSubmitterId(String creatorId) {
        containerRegistrationReportingRequest.setSubmitterId(creatorId);
        return this;
    }

    public ContainerRegistrationReportingRequestBuilder withSubmitterRole(String role) {
        containerRegistrationReportingRequest.setSubmitterRole(role);
        return this;
    }
}
