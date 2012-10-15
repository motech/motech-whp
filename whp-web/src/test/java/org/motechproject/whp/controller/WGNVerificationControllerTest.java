package org.motechproject.whp.controller;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.motechproject.whp.wgninbound.verification.ProviderVerification;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class WGNVerificationControllerTest {

    @Mock
    ProviderVerification providerVerification;
    WGNVerificationController wgnVerificationController;

    @Before
    public void setup() {
        initMocks(this);
        wgnVerificationController = new WGNVerificationController(providerVerification);
    }

    @Test
    public void shouldRespondWithSuccessIfVerificationIsSuccessfulForProvider() throws Exception {
        String phoneNumberFromRequestFile = "0986754322";
        String timeFromRequestFile = "14/08/2012 11:20:59";
        String callIdFromRequestFile = "64756435684375";

        VerificationResult successResult = new VerificationResult(phoneNumberFromRequestFile);
        ProviderVerificationRequest request = new ProviderVerificationRequest(phoneNumberFromRequestFile, timeFromRequestFile, callIdFromRequestFile);

        when(providerVerification.verifyResult(request)).thenReturn(successResult);

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                ).andExpect(
                content().string(containsString("success"))
        );
    }

    @Test
    public void shouldRespondWithFailureIfVerificationFailedProvider() throws Exception {
        String phoneNumberFromRequestFile = "0986754322";
        String timeFromRequestFile = "14/08/2012 11:20:59";
        String callIdFromRequestFile = "64756435684375";

        VerificationResult successResult = new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER), phoneNumberFromRequestFile);
        ProviderVerificationRequest request = new ProviderVerificationRequest(phoneNumberFromRequestFile, timeFromRequestFile, callIdFromRequestFile);

        when(providerVerification.verifyResult(request)).thenReturn(successResult);

        standaloneSetup(wgnVerificationController)
                .build()
                .perform(
                        post("/sputumCall/provider/verify")
                                .body(readXML("/validProviderAuthorizationRequest.xml"))
                ).andExpect(
                content().string(AllOf.allOf(containsString("failure"), containsString("INVALID_PHONE_NUMBER"), containsString("No provider found for the given phone number")))
        );
    }

    private byte[] readXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }

}
