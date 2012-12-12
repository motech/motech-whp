package org.motechproject.whp.it.controller;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.MvcResult;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.xmlConfigSetup;

public class AdherenceIVRControllerIT {

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = xmlConfigSetup("classpath:WEB-INF/spring/webmvc-config.xml", "classpath:META-INF/spring/applicationContext.xml").build();
    }

    @Test
    public void shouldValidateAPIKeyForAdherenceSummary() throws Exception {
        mvc.perform(
                post("/ivr/adherence/summary")
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<adherence_capture_flashing_request></adherence_capture_flashing_request>".getBytes())
                        .header("api-key", "invalid")
        ).andExpect(status().isUnauthorized());
        MvcResult mvcResult = mvc.perform(
                post("/ivr/adherence/summary")
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<adherence_capture_flashing_request></adherence_capture_flashing_request>".getBytes())
                        .header("api-key", "8dc7311c-42c2-4f03-ac86-f1de34416ca4")
        ).andReturn();
        assertFalse(mvcResult.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldValidateAPIKeyForAdherenceValidation() throws Exception {
        mvc.perform(
                post("/ivr/adherence/validate")
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<adherence_validation_request></adherence_validation_request>".getBytes())
                        .header("api-key", "invalid")
        ).andExpect(status().isUnauthorized());
        MvcResult mvcResult = mvc.perform(
                post("/ivr/adherence/validate")
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<adherence_validation_request></adherence_validation_request>".getBytes())
                        .header("api-key", "8dc7311c-42c2-4f03-ac86-f1de34416ca4")
        ).andReturn();
        assertFalse(mvcResult.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED);
    }
}
