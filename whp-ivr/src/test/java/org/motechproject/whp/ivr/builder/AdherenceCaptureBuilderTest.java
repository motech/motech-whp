package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdherenceCaptureBuilderTest {

    private static final String CHANNEL = "IVR";
    private static final boolean VALID = true;

    private static final String PROVIDER_ID = "providerId";
    private static final String PATIENT_ID = "patientId";

    private static final String INPUT_FOR_CURRENT_PATIENT = "1";
    private static final String ADHERENCE_VALUE = "TAKEN";
    private static final String MOBILE_NUMBER = "mobileNumber";

    public static final String CALL_ID = "callId";

    private IvrSession ivrSession;

    @Before
    public void setup() {
        FlowSessionStub flowSession = new FlowSessionStub();
        flowSession.set("cid", MOBILE_NUMBER);

        ivrSession = new IvrSession(flowSession);
        ivrSession.providerId(PROVIDER_ID);
        ivrSession.adherenceInputForCurrentPatient(INPUT_FOR_CURRENT_PATIENT);
        ivrSession.callId(CALL_ID);
    }

    @Test
    public void shouldSetChannelToIVRByDefault() {
        assertEquals(CHANNEL, new AdherenceCaptureBuilder().build().getChannelId());
    }

    @Test
    public void shouldSetCaptureToValidByDefault() {
        assertEquals(VALID, new AdherenceCaptureBuilder().build().isValid());
    }

    @Test
    public void shouldSetPatientId() {
        assertEquals(PATIENT_ID, new AdherenceCaptureBuilder().forPatient(PATIENT_ID).build().getPatientId());
    }

    @Test
    public void shouldSetProviderId() {
        assertEquals(PROVIDER_ID, new AdherenceCaptureBuilder().forSession(ivrSession).build().getProviderId());
    }

    @Test
    public void shouldSetAdherenceValue() {
        assertNull(new AdherenceCaptureBuilder().build().getStatus());
        assertEquals(ADHERENCE_VALUE, new AdherenceCaptureBuilder().forSession(ivrSession).build().getStatus());
    }

    @Test
    public void shouldSetEnteredInput() {
        assertNull(new AdherenceCaptureBuilder().build().getStatus());
        assertEquals(new Integer(1), new AdherenceCaptureBuilder().forSession(ivrSession).build().getSubmittedValue());
    }

    @Test
    public void shouldSetMobileNumber() {
        assertEquals(MOBILE_NUMBER, new AdherenceCaptureBuilder().forSession(ivrSession).build().getSubmittedBy());
    }

    @Test
    public void shouldSetCallId() {
        assertEquals(CALL_ID, new AdherenceCaptureBuilder().forSession(ivrSession).build().getCallId());
    }
}
