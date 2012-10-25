package org.motechproject.whp.containerregistration.api.webservice;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containerregistration.api.webservice.IVRContainerRegistrationController;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.MockMvcResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                MockMvcResultMatchers.content().string(containsString("success"))
        );
    }

    @Test
    public void shouldRespondWithBadRequestIfRequestXMLIsInvalid() throws Exception {
        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/provider/verify")
                                .body(readXML("/inValidProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
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

        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content().string(
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

        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/container/verify")
                                .body(readXML("/validContainerAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                MockMvcResultMatchers.content().string(containsString("success"))
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

        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/container/verify")
                                .body(readXML("/validContainerAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content().string(
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
        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/container/verify")
                                .body("invalidXMLContent".getBytes())
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldRegisterContainerOnSuccessfulValidation() throws Exception {

        String providerId = "providerId";
        String msisdn = "0986754322";
        String phase = "PreTreatment";
        String containerId = "76862367681";
        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest();
        request.setMsisdn(msisdn);
        request.setContainer_id(containerId);
        request.setPhase(phase);
        request.setCall_id("64756435684375");
        when(containerRegistrationVerification.verifyRequest((IvrContainerRegistrationRequest) anyObject())).thenReturn(new VerificationResult());
        Provider provider = new Provider();
        provider.setProviderId(providerId);
        when(providerService.findByMobileNumber(anyString())).thenReturn(provider);

        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/registerContainer")
                                .body(readXML("/validIVRContainerRegistrationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("success"))
        );

        ArgumentCaptor<ContainerRegistrationRequest> containerRegistrationRequestArgumentCaptor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(containerService, times(1)).registerContainer(containerRegistrationRequestArgumentCaptor.capture());
        assertEquals(phase, containerRegistrationRequestArgumentCaptor.getValue().getInstance());
        assertEquals(providerId.toLowerCase(), containerRegistrationRequestArgumentCaptor.getValue().getProviderId());
        assertEquals(containerId, containerRegistrationRequestArgumentCaptor.getValue().getContainerId());

    }

    @Test
    public void shouldNotRegisterContainerForInvalidContainerDetails() throws Exception {

        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest();
        request.setMsisdn("0986754322");
        request.setContainer_id("76862367681");
        request.setPhase("Pre-treatment");
        request.setCall_id("64756435684375");
        when(containerRegistrationVerification.verifyRequest((IvrContainerRegistrationRequest) anyObject())).thenReturn(new VerificationResult(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID)));
        MockMvcBuilders.standaloneSetup(IVRContainerRegistrationController)
                .build()
                .perform(
                        MockMvcRequestBuilders.post("/sputumCall/registerContainer")
                                .body(readXML("/validIVRContainerRegistrationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("failure"))
                );

        verify(containerService, never()).registerContainer((ContainerRegistrationRequest) anyObject());
    }

    private byte[] readXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }

}
