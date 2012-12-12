package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static org.junit.Assert.*;

public class AdherenceCaptureReportRequestTest {

    private ProviderId providerId;

    @Before
    public void setup() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().build());
    }

    @Test
    public void shouldHavePatientIdWhenNotValid() {
        String patientId = "patientId";
        AdherenceValidationRequest request = new AdherenceValidationRequest(patientId);

        String requestPatientId = new AdherenceCaptureReportRequest(request, providerId, false).request().getPatientId();
        assertEquals(patientId, requestPatientId);
    }

    @Test
    public void shouldHavePatientIdWhenValid() {
        String patientId = "patientId";
        AdherenceValidationRequest request = new AdherenceValidationRequest(patientId);

        String requestPatientId = new AdherenceCaptureReportRequest(request, providerId, true).request().getPatientId();
        assertEquals(patientId, requestPatientId);
    }

    @Test
    public void shouldBeInvalid() {
        boolean valid = false;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        boolean requestValid = new AdherenceCaptureReportRequest(request, providerId, valid).request().isValid();
        assertFalse(requestValid);
    }

    @Test
    public void shouldBeValid() {
        boolean valid = true;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        boolean requestValid = new AdherenceCaptureReportRequest(request, providerId, valid).request().isValid();
        assertTrue(requestValid);
    }

    @Test
    public void shouldHaveStatusValid() {
        boolean valid = true;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        String requestStatus = new AdherenceCaptureReportRequest(request, providerId, valid).request().getStatus();
        assertEquals("Valid", requestStatus);
    }

    @Test
    public void shouldHaveStatusInvalid() {
        boolean valid = false;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        String requestStatus = new AdherenceCaptureReportRequest(request, providerId, valid).request().getStatus();
        assertEquals("Invalid", requestStatus);
    }

    @Test
    public void shouldHaveProviderId() {
        boolean valid = true;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        String requestProviderId = new AdherenceCaptureReportRequest(request, providerId, valid).request().getProviderId();
        assertEquals(providerId.value(), requestProviderId);
    }

    @Test
    public void shouldHaveSubmitter() {
        boolean valid = true;
        AdherenceValidationRequest request = new AdherenceValidationRequest("patientId");

        String submittedBy = new AdherenceCaptureReportRequest(request, providerId, valid).request().getSubmittedBy();
        assertEquals(providerId.value(), submittedBy);
    }

    @Test
    public void shouldHaveSubmittedValue() {
        String submittedValue = "1";
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        request.setDoseTakenCount(submittedValue);

        String requestValue = new AdherenceCaptureReportRequest(request, providerId, true).request().getSubmittedValue();
        assertEquals(submittedValue, requestValue);
    }

    @Test
    public void shouldHaveTimeTaken() {
        String timeTaken = "1";
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        request.setTimeTaken(timeTaken);

        Long requestValue = new AdherenceCaptureReportRequest(request, providerId, true).request().getTimeTaken();
        assertEquals(timeTaken, requestValue.toString());
    }

    @Test
    public void shouldHaveChannelId() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        String requestChannel = new AdherenceCaptureReportRequest(request, providerId, true).request().getChannelId();
        assertEquals("IVR", requestChannel);
    }

    @Test
    public void shouldHaveCallId() {
        String callId = "1";
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        request.setCallId(callId);

        String requestCallId = new AdherenceCaptureReportRequest(request, providerId, true).request().getCallId();
        assertEquals(callId, requestCallId);
    }
}
