package org.motechproject.whp.adherenceapi.webservice;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
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
    private AdherenceService adherenceService;

    @Mock
    private ProviderService providerService;

    @Mock
    private AdherenceWindow adherenceWindow;

    private LocalDate today = new LocalDate(2012, 12, 5);

    private AdherenceAPIController adherenceAPIController;

    @Before
    public void setup() {
        initMocks(this);
        mockCurrentDate(today);
        adherenceAPIController = new AdherenceAPIController(adherenceService, providerService, adherenceWindow);
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

        String msisdn = "0986754322";
        String providerId = "providerid";
        Provider provider = new ProviderBuilder().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();

        when(providerService.findByMobileNumber(msisdn)).thenReturn(provider);
        when(adherenceWindow.isValidAdherenceDay(today)).thenReturn(true);
        when(adherenceService.adherenceSummary(providerId, today)).thenReturn(adherenceFlashingResponse);

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

    @Test
    public void shouldValidateMSISDN() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<adherence_capture_flashing_request>\n" +
                " <msisdn>0986754322</msisdn>\n" +
                " <call_id>abcd1234</call_id>\n" +
                " <call_time>14/08/2012 11:20:59</call_time>\n" +
                "</adherence_capture_flashing_request>";


        when(providerService.findByMobileNumber("0986754322")).thenReturn(null);
        when(adherenceWindow.isValidAdherenceDay(today)).thenReturn(true);

        String expectedXml =
                "            <adherence_capture_flashing_response>" +
                        "      <result>failure</result>" +
                        "      <error_code>INVALID_MOBILE_NUMBER</error_code>" +
                        "    </adherence_capture_flashing_response>";

        standaloneSetup(adherenceAPIController)
                .build()
                .perform(post("/adherenceSubmission/").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithInvalidAdherenceDate() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<adherence_capture_flashing_request>\n" +
                " <msisdn>0986754322</msisdn>\n" +
                " <call_id>abcd1234</call_id>\n" +
                " <call_time>14/08/2012 11:20:59</call_time>\n" +
                "</adherence_capture_flashing_request>";


        Provider provider = new ProviderBuilder().withDefaults().withId("providerId").build();
        when(providerService.findByMobileNumber("0986754322")).thenReturn(provider);
        when(adherenceWindow.isValidAdherenceDay(today)).thenReturn(false);

        String expectedXml =
                "            <adherence_capture_flashing_response>" +
                        "      <result>failure</result>" +
                        "      <error_code>NON_ADHERENCE_DAY</error_code>" +
                        "    </adherence_capture_flashing_response>";

        standaloneSetup(adherenceAPIController)
                .build()
                .perform(post("/adherenceSubmission/").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

}
