package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static junit.framework.Assert.assertEquals;

public class AdherenceCaptureReportRequestTest {

    @Test
    public void shouldReportChannelAsIVR() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        ProviderId providerId = new ProviderId();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId);

        assertEquals("IVR", reportRequest.captureRequest().getChannelId());
    }

    @Test
    public void shouldReportTimeTaken() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        request.setTimeTaken("10");

        ProviderId providerId = new ProviderId();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId);

        assertEquals(new Long(10l), reportRequest.captureRequest().getTimeTaken());
    }

    @Test
    public void shouldReportTimeTakenAsZeroWhenTimeTakenIsNull() {
        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest();

        assertEquals(new Long(0l), reportRequest.withTimeTaken(null).getTimeTaken());
    }

    @Test
    public void shouldReportCallId() {
        ProviderId providerId = new ProviderId();

        AdherenceValidationRequest request = new AdherenceValidationRequest();
        String callId = "callId";
        request.setCallId(callId);

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId);

        assertEquals(callId, reportRequest.captureRequest().getCallId());
    }

    @Test
    public void shouldReportCallStatusAsValid() {
        ProviderId providerId = new ProviderId();

        AdherenceValidationRequest request = new AdherenceValidationRequest();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, true);

        assertEquals("VALID", reportRequest.captureRequest().getStatus());
    }

    @Test
    public void shouldReportCallStatusAsInvalid() {
        ProviderId providerId = new ProviderId();

        AdherenceValidationRequest request = new AdherenceValidationRequest();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, false);

        assertEquals("INVALID", reportRequest.captureRequest().getStatus());
    }

    @Test
    public void shouldReportValidationRequestWithProviderId() {
        ProviderId providerId = new ProviderId(new ProviderBuilder().withProviderId("providerId").build());

        AdherenceValidationRequest request = new AdherenceValidationRequest();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, false);

        assertEquals(providerId.value(), reportRequest.captureRequest().getProviderId());
    }

    @Test
    public void shouldReportPatientId() {
        ProviderId providerId = new ProviderId();

        AdherenceValidationRequest request = new AdherenceValidationRequest();
        String patientId = "patientId";
        request.setPatientId(patientId);

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, false);

        assertEquals(patientId, reportRequest.captureRequest().getPatientId());
    }

    @Test
    public void shouldReportDosesTaken() {
        ProviderId providerId = new ProviderId();

        AdherenceValidationRequest request = new AdherenceValidationRequest();
        String dosesTaken = "1";
        request.setDoseTakenCount(dosesTaken);

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, false);

        assertEquals(dosesTaken, reportRequest.captureRequest().getSubmittedValue());
    }

    @Test
    public void shouldReportProviderAsSubmitterOfAdherenceValidationRequest() {
        ProviderId providerId = new ProviderId(new ProviderBuilder().withProviderId("providerId").build());

        AdherenceValidationRequest request = new AdherenceValidationRequest();

        AdherenceCaptureReportRequest reportRequest = new AdherenceCaptureReportRequest(request, providerId, false);

        assertEquals(providerId.value(), reportRequest.captureRequest().getSubmittedBy());
    }
}
