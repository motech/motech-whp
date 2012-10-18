package org.motechproject.whp.mapper;


import org.junit.Test;
import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;

import static junit.framework.Assert.assertEquals;

public class ContainerVerificationRequestMapperTest {

    @Test
    public void shouldMapContainerVerificationRequestFromIVRContainerRegistrationRequest() {
        IvrContainerRegistrationRequest ivrContainerRegistrationRequest = new IvrContainerRegistrationRequest();
        ivrContainerRegistrationRequest.setCall_id("call_id");
        ivrContainerRegistrationRequest.setContainer_id("container_id");
        ivrContainerRegistrationRequest.setMsisdn("msisdn");
        ivrContainerRegistrationRequest.setPhase("phase");

        ContainerVerificationRequestMapper containerVerificationRequestMapper = new ContainerVerificationRequestMapper();
        ContainerVerificationRequest containerVerificationRequest;
        containerVerificationRequest = containerVerificationRequestMapper.buildContainerVerificationRequest(ivrContainerRegistrationRequest);

        assertEquals("container_id", containerVerificationRequest.getContainer_id());
        assertEquals("call_id",containerVerificationRequest.getCall_id());
        assertEquals("msisdn", containerVerificationRequest.getMsisdn());
    }
}
