package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.reports.contract.ContainerStatusReportingRequest;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;

public class ContainerStatusReportingRequestBuilder {
    private ContainerStatusReportingRequest reportingRequest;

    public ContainerStatusReportingRequestBuilder() {
        reportingRequest = new ContainerStatusReportingRequest();
    }

    public ContainerStatusReportingRequestBuilder forContainer(Container container) {
        reportingRequest.setContainerId(container.getContainerId());
        reportingRequest.setDiagnosis(container.getDiagnosis() != null ? container.getDiagnosis().name() : null);
        reportingRequest.setAlternateDiagnosisCode(container.getAlternateDiagnosis());
        reportingRequest.setClosureDate(container.getStatus() == Closed ? today().toDate() : null);
        reportingRequest.setConsultationDate(container.getConsultationDate() != null ? container.getConsultationDate().toDate() : null);
        reportingRequest.setContainerStatus(container.getStatus().name());
        reportingRequest.setReasonForClosure(container.getReasonForClosure());
        return this;
    }

    public ContainerStatusReportingRequest build() {
        return reportingRequest;
    }
}
