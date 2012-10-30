package org.motechproject.whp.containerregistration.api.webservice;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;
import org.springframework.http.MediaType;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class IVRContainerRegistrationControllerTest {

    @Mock
    ProviderVerification providerVerification;

    @Mock
    ContainerVerification containerVerification;

    @Mock
    ContainerService containerService;

    @Mock
    ProviderService providerService;

    org.motechproject.whp.containerregistration.api.webservice.IVRContainerRegistrationController IVRContainerRegistrationController;

    @Mock
    ContainerRegistrationVerification containerRegistrationVerification;

    @Before
    public void setup() {
        initMocks(this);
        IVRContainerRegistrationController = new IVRContainerRegistrationController(providerVerification, containerVerification, containerRegistrationVerification, containerService, providerService);
    }

    @Test
    public void shouldRespondWithSuccessIfVerificationIsSuccessfulForProvider() throws Exception {
        String phoneNumberFromRequestFile = "0986754322";
        String timeFromRequestFile = "14/08/2012 11:20:59";
        String callIdFromRequestFile = "64756435684375";

        VerificationResult successResult = new VerificationResult();
        ProviderVerificationRequest request = new ProviderVerificationRequest(phoneNumberFromRequestFile, timeFromRequestFile, callIdFromRequestFile);

        when(providerVerification.verifyRequest(request)).thenReturn(successResult);

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                content().string(containsString("success"))
        );
    }

    @Test
    public void shouldRespondWithBadRequestIfRequestXMLIsInvalid() throws Exception {
        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/provider/verify")
                                .body(readXML("/inValidProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void shouldRespondWithFailureIfVerificationFailedProvider() throws Exception {
        String phoneNumberFromRequestFile = "0986754322";
        String timeFromRequestFile = "14/08/2012 11:20:59";
        String callIdFromRequestFile = "64756435684375";

        VerificationResult successResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        ProviderVerificationRequest request = new ProviderVerificationRequest(phoneNumberFromRequestFile, timeFromRequestFile, callIdFromRequestFile);

        when(providerVerification.verifyRequest(request)).thenReturn(successResult);

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                allOf(
                                        containsString("failure"),
                                        containsString("INVALID_PHONE_NUMBER"),
                                        containsString("No provider found for the given phone number")
                                )
                        )
                );
    }

    @Test
    public void shouldRespondWithSuccessIfVerificationOfContainerPassed() throws Exception {
        String msisdnFromFile = "0986754322";
        String containerIdFromFile = "1234567890";
        String callIdFromFile = "64756435684375";

        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest(msisdnFromFile, containerIdFromFile, callIdFromFile);
        when(containerVerification.verifyRequest(containerVerificationRequest)).thenReturn(new VerificationResult());

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/container/verify")
                                .body(readXML("/validContainerAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                content().string(containsString("success"))
        );
    }

    @Test
    public void shouldRespondWithFailureIfVerificationOfContainerFailed() throws Exception {
        String msisdnFromFile = "0986754322";
        String containerIdFromFile = "1234567890";
        String callIdFromFile = "64756435684375";

        WHPError whpError = new WHPError(WHPErrorCode.INVALID_CONTAINER_ID);
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest(msisdnFromFile, containerIdFromFile, callIdFromFile);
        when(containerVerification.verifyRequest(containerVerificationRequest)).thenReturn(new VerificationResult(whpError));

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/container/verify")
                                .body(readXML("/validContainerAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                allOf(
                                        containsString("failure"),
                                        containsString(whpError.getErrorCode().name()),
                                        containsString(whpError.getMessage())
                                )
                        )
                );
    }

    @Test
    public void shouldRespondWithErrorOnInvalidXMLForContainerRequest() throws Exception {
        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/container/verify")
                                .body("invalidXMLContent".getBytes())
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterContainerOnSuccessfulValidation() throws Exception {
        Provider provider = new Provider();
        provider.setProviderId("providerId");

        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest("0986754322", "76862367681", "64756435684375", "PreTreatment");
        when(containerRegistrationVerification.verifyRequest((IvrContainerRegistrationRequest) anyObject())).thenReturn(new VerificationResult());
        when(providerService.findByMobileNumber(anyString())).thenReturn(provider);

        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/register")
                                .body(readXML("/validIVRContainerRegistrationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)

                ).andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("success"))
        );

        ArgumentCaptor<ContainerRegistrationRequest> containerRegistrationRequestArgumentCaptor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(containerService, times(1)).registerContainer(containerRegistrationRequestArgumentCaptor.capture());
        ContainerRegistrationRequest containerRegistrationRequest = containerRegistrationRequestArgumentCaptor.getValue();

        assertEquals(request.getContainer_id(), containerRegistrationRequest.getContainerId());
        assertEquals(request.getPhase(), containerRegistrationRequest.getInstance());
        assertEquals(provider.getProviderId(), containerRegistrationRequest.getProviderId());
        assertEquals(ChannelId.IVR.name(), containerRegistrationRequest.getChannelId());
        assertEquals(provider.getProviderId(), containerRegistrationRequest.getSubmitterId());
        assertEquals(WHPRole.PROVIDER.name(), containerRegistrationRequest.getSubmitterRole());
    }

    @Test
    public void shouldNotRegisterContainerForInvalidContainerDetails() throws Exception {

        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest();
        request.setMsisdn("0986754322");
        request.setContainer_id("76862367681");
        request.setPhase("Pre-treatment");
        request.setCall_id("64756435684375");
        when(containerRegistrationVerification.verifyRequest((IvrContainerRegistrationRequest) anyObject())).thenReturn(new VerificationResult(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID)));
        standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        post("/ivr/containerRegistration/register")
                                .body(readXML("/validIVRContainerRegistrationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("failure"))
                );

        verify(containerService, never()).registerContainer((ContainerRegistrationRequest) anyObject());
    }

    private byte[] readXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }

}
