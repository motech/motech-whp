package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdherenceConfirmationRequestTest {
    @Test
    public void shouldReturnValidationRequestFromConfirmationRequest() {
        String callId = "callId";
        String doseTaken = "1";
        String providerId = "raj";
        String patientId = "patientId";
        String timeTaken = "1234";

        AdherenceConfirmationRequest confirmationRequest = new AdherenceConfirmationRequest();
        confirmationRequest.setCallId(callId);
        confirmationRequest.setDoseTakenCount(doseTaken);
        confirmationRequest.setProviderId(providerId);
        confirmationRequest.setPatientId(patientId);
        confirmationRequest.setTimeTaken(timeTaken);

        AdherenceValidationRequest validationRequest = confirmationRequest.validationRequest();

        assertEquals(callId, validationRequest.getCallId());
        assertEquals(doseTaken, validationRequest.getDoseTakenCount());
        assertEquals(providerId, validationRequest.getProviderId());
        assertEquals(patientId, validationRequest.getPatientId());
        assertEquals(timeTaken, validationRequest.getTimeTaken());
    }
}
