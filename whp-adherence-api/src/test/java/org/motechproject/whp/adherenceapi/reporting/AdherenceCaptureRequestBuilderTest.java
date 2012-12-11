package org.motechproject.whp.adherenceapi.reporting;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static org.junit.Assert.*;
import static org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureRequestBuilder.*;

public class AdherenceCaptureRequestBuilderTest {

    private static final String CHANNEL = "IVR";

    private static final String PROVIDER_ID = "providerid";

    private static final String PATIENT_ID = "patientId";

    private static final String INPUT_FOR_CURRENT_PATIENT = "1";

    private static final String MOBILE_NUMBER = "1234567890";

    private static final String CALL_ID = "callId";

    private static final String TIME_TAKEN = "1";

    private ProviderId providerId;

    private AdherenceValidationRequest request;

    @Before
    public void setup() {
        setupRequest();
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withProviderId(PROVIDER_ID).build());
    }

    private void setupRequest() {
        request = new AdherenceValidationRequest();
        request.setPatientId(PATIENT_ID);
        request.setTimeTaken(TIME_TAKEN);
        request.setCallId(CALL_ID);
        request.setDoseTakenCount(INPUT_FOR_CURRENT_PATIENT);
        request.setMsisdn(MOBILE_NUMBER);
    }

    @Test
    public void shouldSetChannelToIVRByDefault() {
        assertEquals(CHANNEL, new AdherenceCaptureRequestBuilder().validAdherence(request, providerId).getChannelId());
    }

    @Test
    public void shouldBuildRequestForValidAdherence() {
        AdherenceCaptureRequest adherenceCaptureReportRequest = new AdherenceCaptureRequestBuilder().validAdherence(request, providerId);

        assertTrue(adherenceCaptureReportRequest.isValid());
        assertEquals(PATIENT_ID, adherenceCaptureReportRequest.getPatientId());
        assertEquals(PROVIDER_ID, adherenceCaptureReportRequest.getProviderId());
        assertEquals(VALID, adherenceCaptureReportRequest.getStatus());
        assertEquals(INPUT_FOR_CURRENT_PATIENT, adherenceCaptureReportRequest.getSubmittedValue());
        assertEquals(MOBILE_NUMBER, adherenceCaptureReportRequest.getSubmittedBy());
        assertEquals(CALL_ID, adherenceCaptureReportRequest.getCallId());
        assertEquals(VALID, adherenceCaptureReportRequest.getStatus());
        assertEquals(new Long(1), adherenceCaptureReportRequest.getTimeTaken());
    }

    @Test
    public void shouldBuildRequestForInvalidAdherence() {
        AdherenceCaptureRequest adherenceCaptureRequest = new AdherenceCaptureRequestBuilder().invalidAdherence(request, providerId);

        assertFalse(adherenceCaptureRequest.isValid());
        assertEquals(INVALID, adherenceCaptureRequest.getStatus());
    }
}
