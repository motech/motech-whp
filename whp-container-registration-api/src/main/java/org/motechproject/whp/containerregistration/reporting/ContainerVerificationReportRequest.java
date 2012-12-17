package org.motechproject.whp.containerregistration.reporting;

import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.reports.contract.ContainerVerificationLogRequest;

public class ContainerVerificationReportRequest {

    private ContainerVerificationLogRequest request;

    public ContainerVerificationReportRequest(ContainerVerificationRequest verificationRequest, boolean valid) {
        request = new ContainerVerificationLogRequest();
        request.setCallId(verificationRequest.getCall_id());
        request.setMobileNumber(verificationRequest.getPhoneNumber());
        request.setValidContainer(valid);
    }

    public ContainerVerificationLogRequest request() {
        return request;
    }
}
