package org.motechproject.whp.adherenceapi.request;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherenceapi.webservice.AdherenceIVRController;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.springframework.http.MediaType;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class AdherenceFlashingRequestTest {

    private AdherenceIVRController adherenceIVRController;

    @Before
    public void setup() {
        adherenceIVRController = new AdherenceIVRController(null, null, null, null, null, null);
    }

    @Test
    public void shouldReturnValidPhoneNumberAsValidMSISDN() {
        String msisdn = "1234567890";

        AdherenceFlashingRequest request = new AdherenceFlashingRequest();
        request.setMsisdn(msisdn);

        assertEquals(new PhoneNumber(msisdn).value(), request.getMsisdn());
    }

    @Test
    public void shouldBeInvalidIfMSISDNIsLessThanTenDigits() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<adherence_capture_flashing_request>\n" +
                " <msisdn>0986754</msisdn>\n" +
                " <call_id>abcd1234</call_id>\n" +
                " <call_time>14/08/2012 11:20:59</call_time>\n" +
                "</adherence_capture_flashing_request>";

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post("/ivr/adherence/summary").body(request.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("length must be between 10")));
    }
}
