package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ContainerRegistrationDetails;
import org.motechproject.whp.reports.contract.ContainerRegistrationReportingRequest;
import org.motechproject.whp.reports.contract.UserGivenPatientDetails;

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
        containerRegistrationReportingRequest.setProviderDistrict(container.getDistrict());
        setUserGivenDetails();
    }

    private void setUserGivenDetails() {
        ContainerRegistrationDetails containerRegistrationDetails = container.getContainerRegistrationDetails();
        UserGivenPatientDetails userGivenPatientDetails = new UserGivenPatientDetails();
        if (containerRegistrationDetails.getPatientId() != null) {
            userGivenPatientDetails.setPatientId(containerRegistrationDetails.getPatientId());
        }
        if (containerRegistrationDetails.getPatientName() != null) {
            userGivenPatientDetails.setPatientName(containerRegistrationDetails.getPatientName());
        }
        if (containerRegistrationDetails.getPatientAge() != null) {
            userGivenPatientDetails.setPatientAge(containerRegistrationDetails.getPatientAge());
        }
        if (containerRegistrationDetails.getPatientGender() != null) {
            userGivenPatientDetails.setGender(containerRegistrationDetails.getPatientGender().name());
        }

        containerRegistrationReportingRequest.setUserGivenPatientDetails(userGivenPatientDetails);
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

    public ContainerRegistrationReportingRequestBuilder withCallId(String callId) {
        containerRegistrationReportingRequest.setCallId(callId);
        return this;
    }
}
