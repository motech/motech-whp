package org.motechproject.whp.container.builder.request;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.reports.contract.SputumLabResultsCaptureReportingRequest;

public class SputumLabResultsCaptureReportingRequestBuilder {

    private SputumLabResultsCaptureReportingRequest labResultsCaptureReportingRequest;

    public SputumLabResultsCaptureReportingRequestBuilder() {
        labResultsCaptureReportingRequest = new SputumLabResultsCaptureReportingRequest();
    }

    public SputumLabResultsCaptureReportingRequestBuilder forContainer(Container container) {
        labResultsCaptureReportingRequest.setContainerId(container.getContainerId());
        LabResults labResults = container.getLabResults();
        labResultsCaptureReportingRequest.setCumulativeResult(labResults.getCumulativeResult().name());
        labResultsCaptureReportingRequest.setLabName(labResults.getLabName());
        labResultsCaptureReportingRequest.setLabNumber(labResults.getLabNumber());
        labResultsCaptureReportingRequest.setSmearTestDate1(labResults.getSmearTestDate1().toDate());
        labResultsCaptureReportingRequest.setSmearTestDate2(labResults.getSmearTestDate2().toDate());
        labResultsCaptureReportingRequest.setSmearTestResult1(labResults.getSmearTestResult1().name());
        labResultsCaptureReportingRequest.setSmearTestResult2(labResults.getSmearTestResult2().name());
        labResultsCaptureReportingRequest.setLabResultsCapturedOn(labResults.getCapturedOn());
        return this;
    }

    public SputumLabResultsCaptureReportingRequest build() {
        return labResultsCaptureReportingRequest;
    }
}
