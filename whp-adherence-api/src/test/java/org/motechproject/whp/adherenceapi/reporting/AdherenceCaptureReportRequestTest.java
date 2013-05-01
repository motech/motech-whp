package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;

import static org.junit.Assert.*;

public class AdherenceCaptureReportRequestTest {

    private ProviderId providerId;

    @Before
    public void setup() {
        providerId = new ProviderId("providerId");
    }

    @Test
    public void shouldHavePatientIdWhenNotValid() {
        String patientId = "patientId";
        AdherenceValidationRequest request = validationRequest(patientId);

        String requestPatientId = new AdherenceCaptureReportRequest(request, providerId, false, AdherenceCaptureStatus.VALID).request().getPatientId();
        assertEquals(patientId, requestPatientId);
    }

    @Test
    public void shouldHavePatientIdWhenValid() {
        String patientId = "patientId";
        AdherenceValidationRequest request = validationRequest(patientId);

        String requestPatientId = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getPatientId();
        assertEquals(patientId, requestPatientId);
    }

    @Test
    public void shouldBeInvalid() {
        boolean valid = false;
        AdherenceValidationRequest request = validationRequest("patientId");

        boolean requestValid = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.INVALID).request().isValid();
        assertFalse(requestValid);
    }

    @Test
    public void shouldBeValid() {
        boolean valid = true;
        AdherenceValidationRequest request = validationRequest("patientId");

        boolean requestValid = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.VALID).request().isValid();
        assertTrue(requestValid);
    }

    @Test
    public void shouldHaveStatusValid() {
        boolean valid = true;
        AdherenceValidationRequest request = validationRequest("patientId");

        String requestStatus = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.VALID).request().getStatus();
        assertEquals("Valid", requestStatus);
    }

    @Test
    public void shouldHaveStatusInvalid() {
        boolean valid = false;
        AdherenceValidationRequest request = validationRequest("patientId");

        String requestStatus = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.INVALID).request().getStatus();
        assertEquals("Invalid", requestStatus);
    }

    @Test
    public void shouldHaveProviderId() {
        boolean valid = true;
        AdherenceValidationRequest request = validationRequest("patientId");

        String requestProviderId = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.VALID).request().getProviderId();
        assertEquals(providerId.value(), requestProviderId);
    }

    @Test
    public void shouldHaveSubmitter() {
        boolean valid = true;
        AdherenceValidationRequest request = validationRequest("patientId");

        String submittedBy = new AdherenceCaptureReportRequest(request, providerId, valid, AdherenceCaptureStatus.VALID).request().getSubmittedBy();
        assertEquals(providerId.value(), submittedBy);
    }

    @Test
    public void shouldHaveSubmittedValue() {
        String submittedValue = "1";
        AdherenceValidationRequest request = validationRequest();
        request.setDoseTakenCount(submittedValue);

        String requestValue = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getSubmittedValue();
        assertEquals(submittedValue, requestValue);
    }

    @Test
    public void shouldHaveTimeTaken() {
        String timeTaken = "1";
        AdherenceValidationRequest request = validationRequest();
        request.setTimeTaken(timeTaken);

        Long requestValue = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getTimeTaken();
        assertEquals(timeTaken, requestValue.toString());
    }

    @Test
    public void shouldHaveChannelId() {
        AdherenceValidationRequest request = validationRequest();
        String requestChannel = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getChannelId();
        assertEquals("IVR", requestChannel);
    }

    @Test
    public void shouldHaveCallId() {
        String callId = "1";
        AdherenceValidationRequest request = validationRequest();
        request.setCallId(callId);

        String requestCallId = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getCallId();
        assertEquals(callId, requestCallId);
    }

    @Test
    public void shouldHaveIvrFileLength() {
        AdherenceValidationRequest request = validationRequest();

        Long requestIvrFileLength = new AdherenceCaptureReportRequest(request, providerId, true, AdherenceCaptureStatus.VALID).request().getIvrFileLength();
        assertEquals(Long.parseLong("20"), requestIvrFileLength.longValue());
    }

    private AdherenceValidationRequest validationRequest() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setIvrFileLength("20");
        return adherenceValidationRequest;
    }

    private AdherenceValidationRequest validationRequest(String patientId) {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setIvrFileLength("20");
        return adherenceValidationRequest;
    }
}
