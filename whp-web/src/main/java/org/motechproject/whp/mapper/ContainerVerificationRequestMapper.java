package org.motechproject.whp.mapper;


import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;

public class ContainerVerificationRequestMapper {

    public ContainerVerificationRequest buildContainerVerificationRequest(IvrContainerRegistrationRequest ivrContainerRegistrationRequest) {
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest(ivrContainerRegistrationRequest.getMsisdn(), ivrContainerRegistrationRequest.getContainer_id(), ivrContainerRegistrationRequest.getCall_id());
        return containerVerificationRequest;
    }
}
