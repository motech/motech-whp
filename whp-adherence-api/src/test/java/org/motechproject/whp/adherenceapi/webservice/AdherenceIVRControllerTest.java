package org.motechproject.whp.adherenceapi.webservice;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherenceapi.adherence.*;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.*;
import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceCallStatusValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.http.MediaType;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.INVALID_MOBILE_NUMBER;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.INVALID_PROVIDER;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.NON_ADHERENCE_DAY;
import static org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse.failureResponse;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class AdherenceIVRControllerTest extends BaseUnitTest {

    public static final String IVR_ADHERENCE_FLASHING_PATH = "/ivr/adherence/summary";
    public static final String IVR_ADHERENCE_VALIDATION_PATH = "/ivr/adherence/validate";
    public static final String IVR_ADHERENCE_CONFIRMATION_PATH = "/ivr/adherence/confirm";
    public static final String IVR_ADHERENCE_NOT_CAPTURED_PATH = "/ivr/adherence/notcaptured";
    public static final String IVR_ADHERENCE_CALL_STATUS_PATH = "/ivr/adherence/callstatus";

    public static final String FLASHING_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_capture_flashing_request>\n" +
            " <msisdn>0986754322</msisdn>\n" +
            " <call_id>abcd1234</call_id>\n" +
            " <call_time>14/08/2012 11:20:59</call_time>\n" +
            "</adherence_capture_flashing_request>";

    public static final String VALIDATION_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_validation_request>\n" +
            "    <call_id>abcd1234</call_id>\n" +
            "    <provider_id>raj</provider_id>\n" +
            "    <patient_id>pat1</patient_id>\n" +
            "    <adherence_value>3</adherence_value>\n" +
            "    <time_taken>7</time_taken>\n" +
            "    <ivr_file_length>17</ivr_file_length>\n" +
            "</adherence_validation_request>";

    public static final String CONFIRMATION_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_confirmation_request>\n" +
            "    <call_id>abcd1234</call_id>\n" +
            "    <provider_id>raj</provider_id>\n" +
            "    <patient_id>pat1</patient_id>\n" +
            "    <adherence_value>3</adherence_value>\n" +
            "    <time_taken>7</time_taken>\n" +
            "    <ivr_file_length>17</ivr_file_length>\n" +
            "</adherence_confirmation_request>";

    public static final String NOT_CAPTURED_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_not_captured_request>\n" +
            "    <call_id>abcd1234</call_id>\n" +
            "    <provider_id>raj</provider_id>\n" +
            "    <patient_id>pat1</patient_id>\n" +
            "    <type>SKIP_INPUT</type>\n" +
            "    <time_taken>7</time_taken>\n" +
            "    <ivr_file_length>17</ivr_file_length>\n" +
            "</adherence_not_captured_request>";

    public static final String CALL_STATUS_REQUEST_BODY = "<?xml version=\"1.0\"?>\n" +
            "<adherence_call_status_request>\n" +
            "    <call_id>abcd1234</call_id>\n" +
            "    <flashing_call_id>abcd1234</flashing_call_id>\n" +
            "    <provider_id>cha010</provider_id>\n" +
            "    <attempt_time>10/12/2012 12:32:31</attempt_time>\n" +
            "    <start_time>10/12/2012 12:32:35</start_time>\n" +
            "    <end_time>10/12/2012 12:33:35</end_time>\n" +
            "    <call_status>SUCCESS</call_status>\n" +
            "    <disconnection_type>PROVIDER_HUNGUP</disconnection_type>\n" +
            "    <patient_count>0</patient_count>" +
            "    <call_answered>YES</call_answered>\n" +
            "    <adherence_not_captured_count>1</adherence_not_captured_count>\n" +
            "    <adherence_captured_count>0</adherence_captured_count>\n" +
            "</adherence_call_status_request>";

    @Mock
    private AdherenceSummaryOverIVR adherenceSummaryOverIVR;
    @Mock
    private ProviderService providerService;
    @Mock
    private AdherenceValidationOverIVR adherenceValidationOverIVR;
    @Mock
    private AdherenceConfirmationOverIVR adherenceConfirmationOverIVR;
    @Mock
    private AdherenceNotCapturedOverIVR adherenceNotCapturedOverIVR;
    @Mock
    private AdherenceCallStatusOverIVR adherenceCallStatusOverIVR;

    private LocalDate today = new LocalDate(2012, 12, 5);

    private AdherenceIVRController adherenceIVRController;

    @Before
    public void setup() {
        initMocks(this);
        mockCurrentDate(today);
        adherenceIVRController = new AdherenceIVRController(providerService, adherenceSummaryOverIVR, adherenceValidationOverIVR, adherenceConfirmationOverIVR, adherenceNotCapturedOverIVR, adherenceCallStatusOverIVR);
    }

    @Test
    public void shouldRespondWithAdherenceSubmissionInformation() throws Exception {
        String providerId = "raj";
        AdherenceFlashingResponse adherenceFlashingResponse = new AdherenceFlashingResponse(
                providerId, asList("pat0"), asList("pat0", "pat1", "pat2")
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
                        "                   <provider_id>raj</provider_id>" +
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
        when(adherenceSummaryOverIVR
                .value(any(AdherenceFlashingRequest.class), any(ProviderId.class))
        ).thenReturn(
                failureResponse(NON_ADHERENCE_DAY.name(), new AdherenceFlashingResponse("raj", asList("pat0"), asList("pat0", "pat1", "pat2")))
        );

        String expectedXml =
                "            <adherence_capture_flashing_response>" +
                        "      <result>failure</result>" +
                        "      <error_code>NON_ADHERENCE_DAY</error_code>" +
                        "           <adherence_status>" +
                        "                   <provider_id>raj</provider_id>" +
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
        when(adherenceValidationOverIVR.handleValidationRequest(any(AdherenceValidationRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().success());

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
        when(adherenceValidationOverIVR.handleValidationRequest(any(AdherenceValidationRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().failure());

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

    @Test
    public void shouldRespondWithSuccessOnSuccessfulConfirmation() throws Exception {
        when(adherenceConfirmationOverIVR.confirmAdherence(any(AdherenceConfirmationRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().success());

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_CONFIRMATION_PATH).body(CONFIRMATION_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldRespondWithErrorInCaseOfValidationFailureForConfirmationRequest() throws Exception {
        String expectedXml =
                "            <adherence_validation_response>" +
                        "      <result>failure</result>" +
                        "      <error>" +
                        "       <error_code>INVALID_MOBILE_NUMBER</error_code>" +
                        "      </error>" +
                        "    </adherence_validation_response>";

        when(adherenceConfirmationOverIVR.confirmAdherence(any(AdherenceConfirmationRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().failure(INVALID_MOBILE_NUMBER.name()));

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_CONFIRMATION_PATH).body(CONFIRMATION_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithSuccessOnSuccessfulNotCapturedRequest() throws Exception {
        when(adherenceNotCapturedOverIVR.recordNotCaptured(any(AdherenceNotCapturedRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().success());

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_NOT_CAPTURED_PATH).body(NOT_CAPTURED_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldRespondWithErrorInCaseOfValidationFailureForNotCapturedRequest() throws Exception {
        String expectedXml =
                "            <adherence_validation_response>" +
                        "      <result>failure</result>" +
                        "      <error>" +
                        "       <error_code>INVALID_MOBILE_NUMBER</error_code>" +
                        "      </error>" +
                        "    </adherence_validation_response>";

        when(adherenceNotCapturedOverIVR.recordNotCaptured(any(AdherenceNotCapturedRequest.class), any(ProviderId.class)))
                .thenReturn(new AdherenceValidationResponse().failure(INVALID_MOBILE_NUMBER.name()));

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_NOT_CAPTURED_PATH).body(NOT_CAPTURED_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }

    @Test
    public void shouldRespondWithSuccessOnSuccessfulCallStatusRequest() throws Exception {
        when(adherenceCallStatusOverIVR.recordCallStatus(any(AdherenceCallStatusRequest.class)))
                .thenReturn(AdherenceCallStatusValidationResponse.success());

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_CALL_STATUS_PATH).body(CALL_STATUS_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldRespondWithErrorInCaseOfValidationFailureForCallStatusRequest() throws Exception {
        String expectedXml =
                "            <adherence_call_status_response>" +
                        "      <result>failure</result>" +
                        "      <error>" +
                        "       <error_code>INVALID_PROVIDER</error_code>" +
                        "      </error>" +
                        "    </adherence_call_status_response>";

        when(adherenceCallStatusOverIVR.recordCallStatus(any(AdherenceCallStatusRequest.class)))
                .thenReturn(AdherenceCallStatusValidationResponse.failure(INVALID_PROVIDER.name()));

        standaloneSetup(adherenceIVRController)
                .build()
                .perform(post(IVR_ADHERENCE_CALL_STATUS_PATH).body(CALL_STATUS_REQUEST_BODY.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_XML))
                .andExpect(content().xml(expectedXml.replaceAll(" ", "")));
    }
}
