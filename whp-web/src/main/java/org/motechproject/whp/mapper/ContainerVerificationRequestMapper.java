package org.motechproject.whp.mapper;


import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;

public class ContainerVerificationRequestMapper {

    public ContainerVerificationRequest buildContainerVerificationRequest(IvrContainerRegistrationRequest ivrContainerRegistrationRequest) {
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest(ivrContainerRegistrationRequest.getMsisdn(), ivrContainerRegistrationRequest.getContainer_id(), ivrContainerRegistrationRequest.getCall_id());
        return containerVerificationRequest;
    }
}
