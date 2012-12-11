package org.motechproject.whp.adherenceapi.webservice;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherenceapi.adherence.AdherenceSummaryOverIVR;
import org.motechproject.whp.adherenceapi.adherence.AdherenceValidationOverIVR;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.http.MediaType;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.INVALID_MOBILE_NUMBER;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.NON_ADHERENCE_DAY;
import static org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse.failureResponse;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class AdherenceIVRControllerTest extends BaseUnitTest {

    public static final String IVR_ADHERENCE_FLASHING_PATH = "/ivr/adherence/summary";
    public static final String IVR_ADHERENCE_VALIDATION_PATH = "/ivr/adherence/validate";

    public static final String FLASHING_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_capture_flashing_request>\n" +
            " <msisdn>0986754322</msisdn>\n" +
            " <call_id>abcd1234</call_id>\n" +
            " <call_time>14/08/2012 11:20:59</call_time>\n" +
            "</adherence_capture_flashing_request>";

    public static final String VALIDATION_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_validation_request>\n" +
            "    <call_id>abcd1234</call_id>\n" +
            "    <msisdn>1234567942</msisdn>\n" +
            "    <patient_id>pat1</patient_id>\n" +
            "    <adherence_value>3</adherence_value>\n" +
            "    <time_taken>7</time_taken>\n" +
            "</adherence_validation_request>";

    @Mock
    private AdherenceSummaryOverIVR adherenceSummaryOverIVR;
    @Mock
    private ProviderService providerService;
    @Mock
    private AdherenceValidationOverIVR adherenceValidationOverIVR;

    private LocalDate today = new LocalDate(2012, 12, 5);

    private AdherenceIVRController adherenceIVRController;

    @Before
    public void setup() {
        initMocks(this);
        mockCurrentDate(today);
        adherenceIVRController = new AdherenceIVRController(providerService, adherenceSummaryOverIVR, adherenceValidationOverIVR);
    }

    @Test
    public void shouldRespondWithAdherenceSubmissionInformation() throws Exception {
        AdherenceFlashingResponse adherenceFlashingResponse = new AdherenceFlashingResponse(
                asList("pat0"), asList("pat1", "pat2")
        );
        AdherenceFlashingRequest flashingRequest = new AdherenceFlashingRequest();
        flashingRequest.setMsisdn("0986754322");
        flashingRequest.setCallId("abcd1234");
        flashingRequest.setCallTime("14/08/2012 11:20:59");

        when(adherenceSummaryOverIVR.value(eq(flashingRequest), any(ProviderId.class))).thenReturn(adherenceFlashingResponse);

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

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_FLASHING_PATH).body(FLASHING_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }


    @Test
    public void shouldRespondWithErrorInCaseOfInvalidMobileNumber() throws Exception {
        String expectedXml =
                "            <adherence_capture_flashing_response>" +
                        "      <result>failure</result>" +
                        "      <error_code>INVALID_MOBILE_NUMBER</error_code>" +
                        "    </adherence_capture_flashing_response>";

        when(adherenceSummaryOverIVR.value(any(AdherenceFlashingRequest.class), any(ProviderId.class)))
                .thenReturn(failureResponse(INVALID_MOBILE_NUMBER.name()));

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_FLASHING_PATH).body(FLASHING_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithErrorOnAnInvalidAdherenceDay() throws Exception {
        when(adherenceSummaryOverIVR.value(any(AdherenceFlashingRequest.class), any(ProviderId.class)))
                .thenReturn(failureResponse(NON_ADHERENCE_DAY.name()));

        String expectedXml =
                "            <adherence_capture_flashing_response>" +
                        "      <result>failure</result>" +
                        "      <error_code>NON_ADHERENCE_DAY</error_code>" +
                        "    </adherence_capture_flashing_response>";

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_FLASHING_PATH).body(FLASHING_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithSuccessOnSuccessfulValidation() throws Exception {
        when(adherenceValidationOverIVR.validateInput(any(AdherenceValidationRequest.class), any(ProviderId.class)))
                .thenReturn(AdherenceValidationResponse.success());

        String expectedXML =
                "        <adherence_validation_response>" +
                        "    <result>success</result>" +
                        "</adherence_validation_response>";

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_VALIDATION_PATH).body(VALIDATION_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXML.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithValidationFailure() throws Exception {
        when(adherenceValidationOverIVR.validateInput(any(AdherenceValidationRequest.class), any(ProviderId.class)))
                .thenReturn(AdherenceValidationResponse.failure());

        String expectedXML =
                "        <adherence_validation_response>" +
                        "    <result>failure</result>" +
                        "</adherence_validation_response>";

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_VALIDATION_PATH).body(VALIDATION_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXML.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithBadRequestForInvalidRequest() throws Exception {
        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_VALIDATION_PATH).body("<adherence_validation_request></adherence_validation_request>".getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().type(MediaType.APPLICATION_XML));

    }

    @Test
    public void shouldRespondWithBadRequestForRequestWithInvalidDateFormat() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<adherence_capture_flashing_request>\n" +
                " <msisdn>0986754322</msisdn>\n" +
                " <call_id>abcd1234</call_id>\n" +
                " <call_time>14-08-2012 11:20:59</call_time>\n" +
                "</adherence_capture_flashing_request>";
        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_FLASHING_PATH).body(request.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().type(MediaType.APPLICATION_XML));

    }
}
