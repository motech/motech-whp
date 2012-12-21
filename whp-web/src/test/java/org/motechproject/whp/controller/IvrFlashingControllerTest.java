package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.ivr.IvrCallService;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.request.IvrFlashingWebRequest;
import org.springframework.http.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class IvrFlashingControllerTest {

    private IvrFlashingController ivrFlashingController;

    @Mock
    private IvrCallService ivrCallService;

    @Before
    public void setUp() {
        initMocks(this);
        ivrFlashingController = new IvrFlashingController(ivrCallService);
    }

    @Test
    public void shouldHandleCallBackRequest() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<missed_call >\n" +
                "<call_no>0986754322</call_no>\n" +
                "<call_id>abcd1234</call_id>\n" +
                "<time>14/08/2012 11:20:59</time>\n" +
                "</missed_call>";


        standaloneSetup(ivrFlashingController).build()
                .perform(post("/ivr/callback").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        ArgumentCaptor<FlashingRequest> flashingRequestCaptor = ArgumentCaptor.forClass(FlashingRequest.class);
        verify(ivrCallService).handleFlashingRequest(flashingRequestCaptor.capture());
        FlashingRequest flashingRequest = flashingRequestCaptor.getValue();

        assertEquals("abcd1234", flashingRequest.getCallId());
        assertEquals("0986754322", flashingRequest.getMobileNumber());
        assertEquals(new WHPDateTime("14/08/2012 11:20:59").dateTime(), flashingRequest.getCallTime());
    }

    @Test
    public void shouldTrimMobileNumberTo10DigitBeforeInitiatingCall() {
        String mobileNumber = "+911234567890";
        String trimmedMobileNumber = "1234567890";

        String callTime = "14/08/2012 11:20:59";

        IvrFlashingWebRequest flashingWebRequest = getFlashingRequest(mobileNumber, callTime);
        ivrFlashingController.callBack(flashingWebRequest);

        FlashingRequest flashingRequest = flashingWebRequest.createFlashingRequest();
        verify(ivrCallService).handleFlashingRequest(flashingRequest);
        assertThat(flashingRequest.getMobileNumber(), is(trimmedMobileNumber));
    }

    private IvrFlashingWebRequest getFlashingRequest(String mobileNumber, String callTime) {
        IvrFlashingWebRequest ivrFlashingWebRequest = new IvrFlashingWebRequest();
        ivrFlashingWebRequest.setMobileNumber(mobileNumber);
        ivrFlashingWebRequest.setCallTime(callTime);
        return ivrFlashingWebRequest;
    }
}
