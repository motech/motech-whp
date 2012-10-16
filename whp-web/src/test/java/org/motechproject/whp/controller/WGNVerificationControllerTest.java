package org.motechproject.whp.controller;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.motechproject.whp.wgninbound.verification.ContainerVerification;
import org.motechproject.whp.wgninbound.verification.ProviderVerification;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class WGNVerificationControllerTest {

    @Mock
    ProviderVerification providerVerification;
    @Mock
    ContainerVerification containerVerification;

    WGNVerificationController wgnVerificationController;

    @Before
    public void setup() {
        initMocks(this);
        wgnVerificationController = new WGNVerificationController(providerVerification, containerVerification);
    }

    @Test
    public void shouldRespondWithSuccessIfVerificationIsSuccessfulForProvider() throws Exception {
        String phoneNumberFromRequestFile = "0986754322";
        String timeFromRequestFile = "14/08/2012 11:20:59";
        String callIdFromRequestFile = "64756435684375";

        VerificationResult successResult = new VerificationResult();
        ProviderVerificationRequest request = new ProviderVerificationRequest(phoneNumberFromRequestFile, timeFromRequestFile, callIdFromRequestFile);

        when(providerVerification.verifyRequest(request)).thenReturn(successResult);

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                                .contentType(MediaType.APPLICATION_XML)
                ).andExpect(
                content().string(containsString("success"))
        );
    }

    @Test
    public void shouldRespondWithBadRequestIfRequestXMLIsInvalid() throws Exception {
        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/provider/verify")
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

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/provider/verify")
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

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/container/verify")
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

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/container/verify")
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
        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/container/verify")
                                .body("invalidXMLContent".getBytes())
                                .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isBadRequest());
    }

    private byte[] readXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }

}
