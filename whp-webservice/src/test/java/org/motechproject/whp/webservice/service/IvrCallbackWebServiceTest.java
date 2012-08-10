package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class IvrCallbackWebServiceTest {

    private IvrCallbackWebService ivrCallbackWebService;

    @Before
    public void setUp() {
        ivrCallbackWebService = new IvrCallbackWebService();
    }

    @Test
    public void shouldHandleCallBackRequest() throws Exception {
        String phoneNumber = "phoneNumber";
        standaloneSetup(ivrCallbackWebService).build()
                .perform(post("/ivr/callback").param("phoneNumber", phoneNumber))
                .andExpect(status().isOk());
    }

    @Test
    public void should(){

    }
}
