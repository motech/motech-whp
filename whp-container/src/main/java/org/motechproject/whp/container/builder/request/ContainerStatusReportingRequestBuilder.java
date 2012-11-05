package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.ContainerStatusReportingRequest;

public class ContainerStatusReportingRequestBuilder {
    private ContainerStatusReportingRequest reportingRequest;

    public ContainerStatusReportingRequestBuilder() {
        reportingRequest = new ContainerStatusReportingRequest();
    }

    public ContainerStatusReportingRequestBuilder forContainer(Container container) {
        reportingRequest.setContainerId(container.getContainerId());
        reportingRequest.setDiagnosis(container.getDiagnosis() != null ? container.getDiagnosis().name() : null);
        reportingRequest.setAlternateDiagnosisCode(container.getAlternateDiagnosis());
        reportingRequest.setClosureDate(container.getClosureDate());
        reportingRequest.setConsultationDate(container.getConsultationDate() != null ? container.getConsultationDate().toDate() : null);
        reportingRequest.setStatus(container.getStatus().name());
        reportingRequest.setReasonForClosure(container.getReasonForClosure());
        return this;
    }

    public ContainerStatusReportingRequest build() {
        return reportingRequest;
    }
}
