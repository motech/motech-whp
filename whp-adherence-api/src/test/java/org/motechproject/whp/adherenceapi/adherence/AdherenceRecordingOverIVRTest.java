package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

public class AdherenceRecordingOverIVRTest {

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private ReportingPublisherService reportingService;

    private AdherenceRecordingOverIVR adherenceRecordingOverIVR;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceRecordingOverIVR = new AdherenceRecordingOverIVR(adherenceService, reportingService);
    }

    @Test
    public void shouldReturnFailureWhenUnableToFindDosageForPatient() {
        String invalidPatientId = "patientId";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(invalidPatientId);
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(invalidPatientId)).thenReturn(null);
        assertEquals(failure(), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, new ProviderId()));
    }

    @Test
    public void shouldReturnFailureWhenAdherenceInputIsInvalid() {
        String patientId = "patientId";
        Dosage dosage = new DosageBuilder(1).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(failure(dosage), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, new ProviderId()));
    }

    @Test
    public void shouldReturnSuccessWhenAdherenceInputIsValid() {
        String patientId = "patientId";
        Dosage dosage = new DosageBuilder(2).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(success(), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, new ProviderId()));
    }

    @Test
    public void shouldReportAdherenceValidationRequest() {
        ProviderId providerId = new ProviderId();
        AdherenceValidationRequest request = new AdherenceValidationRequest();

        adherenceRecordingOverIVR.validateInput(request, providerId);
        verify(reportingService).reportAdherenceCapture(new AdherenceCaptureReportRequest(request, providerId).captureRequest());
    }
}
