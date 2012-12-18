package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.adherenceapi.validator.AdherenceValidationRequestValidator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

public class AdherenceValidationOverIVRTest {

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private ReportingPublisherService reportingService;
    @Mock
    private PatientService patientService;
    @Mock
    private AdherenceValidationRequestValidator adherenceValidationRequestValidator;

    private ProviderId providerId;
    private AdherenceValidationOverIVR adherenceValidationOverIVR;

    @Before
    public void setUp() {
        initMocks(this);
        initializePatientAndProvider();
        adherenceValidationOverIVR = new AdherenceValidationOverIVR(reportingService, adherenceValidationRequestValidator);
    }

    private void initializePatientAndProvider() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
        Patient patient = new PatientBuilder().withDefaults().withPatientId("patientid").withProviderId(providerId.value()).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldReturnFailureWhenAdherenceInputIsInvalid() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();

        AdherenceValidationResponse response = failure();
        when(adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId)).thenReturn(response);

        assertEquals(response, adherenceValidationOverIVR.validateAdherenceInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnSuccessWhenAdherenceInputIsValid() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();

        AdherenceValidationResponse response = success();
        when(adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId)).thenReturn(response);

        assertEquals(response, adherenceValidationOverIVR.validateAdherenceInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReportAdherenceValidationRequestWhenValidationSuccessful() {
        String patientId = "patientid";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId)).thenReturn(success());

        adherenceValidationOverIVR.handleValidationRequest(adherenceValidationRequest, providerId);
        verify(reportingService).reportAdherenceCapture(new AdherenceCaptureReportRequest(adherenceValidationRequest, providerId, true, AdherenceCaptureStatus.VALID).request());
    }

    @Test
    public void shouldReportAdherenceValidationRequestWhenValidationFailed() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();

        when(adherenceValidationRequestValidator.validate(request, providerId)).thenReturn(failure());

        adherenceValidationOverIVR.handleValidationRequest(request, providerId);
        verify(reportingService).reportAdherenceCapture(new AdherenceCaptureReportRequest(request, providerId, false, AdherenceCaptureStatus.INVALID).request());
    }
}
