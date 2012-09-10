package org.motechproject.whp.controller;

import org.junit.Test;
import org.motechproject.whp.patient.service.treatmentupdate.BaseUnitTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class BaseWebControllerTest extends BaseUnitTest {

    @Test
    public void shouldForwardToErrorPageOnException() throws Exception {
        BaseWebController controller = new BaseWebController(){
            @RequestMapping(value = "test", method = RequestMethod.GET)
            public String throwException() {
                throw new RuntimeException("Expected exception");
            }
        };

        standaloneSetup(controller).build()
                .perform(get("/test"))
                .andExpect(forwardedUrl("errors/error"));
    }

}
