package org.motechproject.whp.adherenceapi.request;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherenceapi.webservice.AdherenceIVRController;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class AdherenceValidationRequestTest {

    private AdherenceIVRController adherenceIVRController;

    @Before
    public void setup() {
        adherenceIVRController = new AdherenceIVRController(null, null, null);
    }

    @Test
    public void shouldBeInvalidIfMSISDNIsLessThanTenDigits() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<adherence_validation_request>\n" +
                "    <call_id>abcd1234</call_id>\n" +
                "    <msisdn>1234567</msisdn>\n" +
                "    <patient_id>pat1</patient_id>\n" +
                "    <adherence_value>3</adherence_value>\n" +
                "    <time_taken>7</time_taken>\n" +
                "</adherence_validation_request>";

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post("/ivr/adherence/validate").body(request.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("length must be between 10")));
    }
}
