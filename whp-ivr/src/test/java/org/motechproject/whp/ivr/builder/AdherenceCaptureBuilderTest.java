package org.motechproject.whp.ivr.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdherenceCaptureBuilderTest {

    @Test
    public void shouldSetChannelToIVRByDefault() {
        assertEquals("IVR", new AdherenceCaptureBuilder().build().getChannelId());
    }

    @Test
    public void shouldSetCaptureToValidByDefault() {
        assertEquals(true, new AdherenceCaptureBuilder().build().isValid());
    }

    @Test
    public void shouldSetPatientId() {
        String patientId = "patientId";
        assertEquals(patientId, new AdherenceCaptureBuilder().forPatient(patientId).build().getPatientId());
    }

    @Test
    public void shouldSetProviderId() {
        String providerId = "providerId";
        assertEquals(providerId, new AdherenceCaptureBuilder().byProvider(providerId).build().getProviderId());
    }

    @Test
    public void shouldSetAdherenceValue() {
        assertNull(new AdherenceCaptureBuilder().build().getStatus());
        assertEquals("TAKEN", new AdherenceCaptureBuilder().withInput(1).build().getStatus());
    }

    @Test
    public void shouldSetEnteredInput() {
        assertNull(new AdherenceCaptureBuilder().build().getStatus());
        assertEquals(new Integer(1), new AdherenceCaptureBuilder().withInput(1).build().getSubmittedValue());
    }

    @Test
    public void shouldSetMobileNumber() {
        String mobileNumber = "mobileNumber";
        assertEquals(mobileNumber, new AdherenceCaptureBuilder().throughMobile(mobileNumber).build().getSubmittedBy());
    }

    @Test
    public void shouldSetCallId() {
        String callId = "callId";
        assertEquals(callId, new AdherenceCaptureBuilder().onCall(callId).build().getCallId());
    }
}
