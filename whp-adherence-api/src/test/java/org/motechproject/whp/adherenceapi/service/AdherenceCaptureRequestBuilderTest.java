package org.motechproject.whp.adherenceapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.INVALID;

public class AdherenceCaptureRequestBuilderTest {
    @Mock
    private ProviderService providerService;
    private AdherenceCaptureRequestBuilder adherenceCaptureRequestBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceCaptureRequestBuilder = new AdherenceCaptureRequestBuilder(providerService);
    }

    @Test
    public void shouldBuildAdherenceCaptureRequestForInValidAdherence() {
        String patientId = "1234";
        String doseTakenCount = "3";
        String callId = "callId";
        String msisdn = "1234567890";
        String timeTaken = "50";
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setPatientId(patientId);
        validationRequest.setDoseTakenCount(doseTakenCount);
        validationRequest.setCallId(callId);
        validationRequest.setMsisdn(msisdn);
        validationRequest.setTimeTaken(timeTaken);

        String providerId = "provideridraj";
        when(providerService.findByMobileNumber(msisdn)).thenReturn(new Provider(providerId, msisdn, null, null));
        AdherenceCaptureRequest adherenceCaptureRequest = adherenceCaptureRequestBuilder.forInvalidCase(validationRequest);

        assertFalse(adherenceCaptureRequest.isValid());
        assertEquals(patientId, adherenceCaptureRequest.getPatientId());
        assertEquals(providerId, adherenceCaptureRequest.getProviderId());
        assertEquals(INVALID.getText(), adherenceCaptureRequest.getStatus());
        assertEquals(doseTakenCount, adherenceCaptureRequest.getSubmittedValue());
        assertEquals(msisdn, adherenceCaptureRequest.getSubmittedBy());
        assertEquals(callId, adherenceCaptureRequest.getCallId());
        assertEquals(timeTaken, adherenceCaptureRequest.getTimeTaken().toString());
    }
}
