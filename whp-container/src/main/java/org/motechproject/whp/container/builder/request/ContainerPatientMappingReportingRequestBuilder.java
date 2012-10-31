package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.ContainerPatientMappingReportingRequest;

import java.util.Date;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;

public class ContainerPatientMappingReportingRequestBuilder {

    private ContainerPatientMappingReportingRequest containerPatientMappingReportingRequest;

    public ContainerPatientMappingReportingRequestBuilder() {
        containerPatientMappingReportingRequest = new ContainerPatientMappingReportingRequest();
    }

    public ContainerPatientMappingReportingRequestBuilder forContainer(Container container) {
        containerPatientMappingReportingRequest.setClosureDate(container.getStatus() == Closed ? today().toDate() : null);
        containerPatientMappingReportingRequest.setConsultationDate(container.getConsultationDate() != null ? container.getConsultationDate().toDate() : null);
        containerPatientMappingReportingRequest.setContainerId(container.getContainerId());
        containerPatientMappingReportingRequest.setMappingInstance(container.getMappingInstance() != null ? container.getMappingInstance().name() : null);
        containerPatientMappingReportingRequest.setPatientId(container.getPatientId());
        containerPatientMappingReportingRequest.setReasonForClosure(container.getReasonForClosure());
        containerPatientMappingReportingRequest.setStatus(container.getStatus().name());
        containerPatientMappingReportingRequest.setTbId(container.getTbId());
        return this;
    }

    public ContainerPatientMappingReportingRequest build() {
        return containerPatientMappingReportingRequest;
    }
}
