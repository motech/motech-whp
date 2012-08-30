package org.motechproject.whp.ivr.builder;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.junit.Assert.*;

public class AdherenceCaptureRequestBuilderTest extends BaseUnitTest {

    private static final String CHANNEL = "IVR";

    private static final String PROVIDER_ID = "providerId";
    private static final String PATIENT_ID = "patientId";

    private static final String INPUT_FOR_CURRENT_PATIENT = "1";
    private static final String ADHERENCE_PROVIDED = "Given";
    private static final String MOBILE_NUMBER = "mobileNumber";

    private static final String CALL_ID = "callId";

    private static final DateTime LAST_SUBMISSION_TIME = new DateTime(2011, 1, 1, 1, 1, 0, 0);
    private static final DateTime NOW = new DateTime(2011, 1, 1, 1, 1, 10, 0);
    private static final Long DIFFERENCE_IN_SECONDS = 10000l;

    private IvrSession ivrSession;
    private static final String SKIPPED = "Skipped";
    private static final String INVALID = "Invalid";

    @Before
    public void setup() {
        mockCurrentDate(NOW);
        setupSession();
    }

    private void setupSession() {
        FlowSessionStub flowSession = new FlowSessionStub();
        flowSession.set("cid", MOBILE_NUMBER);

        ivrSession = new IvrSession(flowSession);
        ivrSession.providerId(PROVIDER_ID);
        ivrSession.startOfAdherenceSubmission(LAST_SUBMISSION_TIME);
        ivrSession.adherenceInputForCurrentPatient(INPUT_FOR_CURRENT_PATIENT);
        ivrSession.callId(CALL_ID);
    }

    @Test
    public void shouldSetChannelToIVRByDefault() {
        assertEquals(CHANNEL, new AdherenceCaptureRequestBuilder().validAdherence("any", ivrSession).getChannelId());
    }

    @Test
    public void shouldBuildRequestForValidAdherence(){
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequestBuilder().validAdherence(PATIENT_ID, ivrSession);

        assertTrue(adherenceCaptureRequest.isValid());
        assertEquals(PATIENT_ID, adherenceCaptureRequest.getPatientId());
        assertEquals(PROVIDER_ID, adherenceCaptureRequest.getProviderId());
        assertEquals(ADHERENCE_PROVIDED, adherenceCaptureRequest.getStatus());
        assertEquals(INPUT_FOR_CURRENT_PATIENT, adherenceCaptureRequest.getSubmittedValue());
        assertEquals(MOBILE_NUMBER, adherenceCaptureRequest.getSubmittedBy());
        assertEquals(CALL_ID, adherenceCaptureRequest.getCallId());
        assertEquals(DIFFERENCE_IN_SECONDS, adherenceCaptureRequest.getTimeTaken());
    }

    @Test
    public void shouldBuildRequestForSkipAdherence(){
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequestBuilder().skipAdherence(PATIENT_ID, ivrSession);

        assertTrue(adherenceCaptureRequest.isValid());
        assertEquals(PATIENT_ID, adherenceCaptureRequest.getPatientId());
        assertEquals(PROVIDER_ID, adherenceCaptureRequest.getProviderId());
        assertEquals(SKIPPED, adherenceCaptureRequest.getStatus());
        assertEquals(IVRInput.SKIP_PATIENT_CODE, adherenceCaptureRequest.getSubmittedValue());
        assertEquals(MOBILE_NUMBER, adherenceCaptureRequest.getSubmittedBy());
        assertEquals(CALL_ID, adherenceCaptureRequest.getCallId());
        assertEquals(DIFFERENCE_IN_SECONDS, adherenceCaptureRequest.getTimeTaken());
    }

    @Test
    public void shouldBuildRequestForInvalidAdherence(){
        String adherenceInput = "8";
        ivrSession.adherenceInputForCurrentPatient(adherenceInput);

        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequestBuilder().invalidAdherence(PATIENT_ID, ivrSession, adherenceInput);

        assertFalse(adherenceCaptureRequest.isValid());
        assertEquals(PATIENT_ID, adherenceCaptureRequest.getPatientId());
        assertEquals(PROVIDER_ID, adherenceCaptureRequest.getProviderId());
        assertEquals(INVALID, adherenceCaptureRequest.getStatus());
        assertEquals(adherenceInput, adherenceCaptureRequest.getSubmittedValue());
        assertEquals(MOBILE_NUMBER, adherenceCaptureRequest.getSubmittedBy());
        assertEquals(CALL_ID, adherenceCaptureRequest.getCallId());
        assertEquals(DIFFERENCE_IN_SECONDS, adherenceCaptureRequest.getTimeTaken());

    }
}
