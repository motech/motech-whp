package org.motechproject.whp.adherenceapi.webservice;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceWebService;
import org.springframework.http.MediaType;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class AdherenceAPIControllerTest extends BaseUnitTest {

    @Mock
    private AdherenceWebService adherenceWebService;

    private LocalDate today = new LocalDate(2012, 12, 5);

    private AdherenceAPIController adherenceAPIController;

    @Before
    public void setup() {
        initMocks(this);
        mockCurrentDate(today);
        adherenceAPIController = new AdherenceAPIController(adherenceWebService);
    }

    @Test
    public void shouldRespondWithAdherenceSubmissionInformation() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<adherence_capture_flashing_request>\n" +
                " <msisdn>0986754322</msisdn>\n" +
                " <call_id>abcd1234</call_id>\n" +
                " <call_time>14/08/2012 11:20:59</call_time>\n" +
                "</adherence_capture_flashing_request>";

        AdherenceCaptureFlashingResponse adherenceFlashingResponse = new AdherenceCaptureFlashingResponse(
                asList("pat0"), asList("pat1", "pat2")
        );

        AdherenceCaptureFlashingRequest flashingRequest = new AdherenceCaptureFlashingRequest();
        flashingRequest.setMsisdn("0986754322");
        flashingRequest.setCallId("abcd1234");
        flashingRequest.setCallTime("14/08/2012 11:20:59");
        when(adherenceWebService.processFlashingRequest(flashingRequest, today)).thenReturn(adherenceFlashingResponse);

        String expectedXml =
                "             <adherence_capture_flashing_response>" +
                        "               <result>success</result>" +
                        "               <adherence_status>" +
                        "                   <patient_remaining_count>2</patient_remaining_count>" +
                        "                   <patient_given_count>1</patient_given_count>" +
                        "                   <patients_remaining>" +
                        "                       <patient>" +
                        "                           <id>pat1</id>" +
                        "                       </patient>" +
                        "                       <patient>" +
                        "                           <id>pat2</id>" +
                        "                       </patient>" +
                        "                   </patients_remaining>" +
                        "                </adherence_status>" +
                        "     </adherence_capture_flashing_response>";

        standaloneSetup(adherenceAPIController)
                .build()
                .perform(post("/adherenceSubmission/").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }
}
