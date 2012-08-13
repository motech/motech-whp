package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.ivr.IvrCallService;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.contract.FlashingRequest;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class IvrFlashingWebServiceTest {

    private IvrFlashingWebService ivrFlashingWebService;

    @Mock
    private IvrCallService ivrCallService;

    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        ivrFlashingWebService = new IvrFlashingWebService(ivrCallService, providerService);
    }

    @Test
    public void shouldHandleCallBackRequest() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<missed_call >\n" +
                "<call_no>0986754322</call_no>\n" +
                "<time>14/08/2012 11:20:59</time>\n" +
                "</missed_call>";

        standaloneSetup(ivrFlashingWebService).build()
                .perform(post("/ivr/callback").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldTrimMobileNumberTo10DigitBeforeInitiatingCall() {
        String mobileNumber = "+911234567890";
        String trimmedMobileNumber = "1234567890";

        when(providerService.isRegisteredMobileNumber(trimmedMobileNumber)).thenReturn(true);

        ivrFlashingWebService.callBack(getFlashingRequest(mobileNumber));
        verify(ivrCallService).initiateCall(trimmedMobileNumber);
    }

    private FlashingRequest getFlashingRequest(String mobileNumber) {
        FlashingRequest flashingRequest = new FlashingRequest();
        flashingRequest.setMobileNumber(mobileNumber);
        return flashingRequest;
    }

    @Test
    public void shouldNotInitiateOutGoingCallToProvider_forUnregisteredMobileNumbers() {
        String unregisteredMobileNumber = "8888";
        when(providerService.isRegisteredMobileNumber(unregisteredMobileNumber)).thenReturn(false);
        ivrFlashingWebService.callBack(getFlashingRequest(unregisteredMobileNumber));

        verify(ivrCallService, never()).initiateCall(anyString());
    }

    @Test
    public void shouldInitiateOutGoingCallToProvider_forRegisteredMobileNumbersOnly() {
        String mobileNumber = "1234567890";
        when(providerService.isRegisteredMobileNumber(mobileNumber)).thenReturn(true);
        ivrFlashingWebService.callBack(getFlashingRequest(mobileNumber));

        verify(ivrCallService).initiateCall(mobileNumber);
    }
}
