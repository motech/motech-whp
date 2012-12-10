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

public class AdherenceRecordingOverIVRTest {

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private ReportingPublisherService reportingService;
    @Mock
    private PatientService patientService;

    private ProviderId providerId;

    private AdherenceRecordingOverIVR adherenceRecordingOverIVR;

    @Before
    public void setUp() {
        initMocks(this);
        initializePatientAndProvider();
        adherenceRecordingOverIVR = new AdherenceRecordingOverIVR(adherenceService, reportingService, patientService);
    }

    private void initializePatientAndProvider() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
        Patient patient = new PatientBuilder().withDefaults().withPatientId("patientid").withProviderId(providerId.value()).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldReturnFailureWhenPhoneNumberDoesNotBelongToAnyProvider() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();
        ProviderId emptyProviderId = new ProviderId();
        assertEquals(failure("INVALID_MOBILE_NUMBER"), adherenceRecordingOverIVR.validateInput(request, emptyProviderId));
    }

    @Test
    public void shouldReturnFailureWhenUnableToFindDosageForPatient() {
        String invalidPatientId = "patientid";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(invalidPatientId);
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(invalidPatientId)).thenReturn(null);
        assertEquals(failure("INVALID_PATIENT"), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureWhenPatientDoesNotBelongToProvider() {
        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withProviderId("someOtherProviderId").build();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);

        when(adherenceService.dosageForPatient(patientId)).thenReturn(new DosageBuilder(1).dosage());
        when(patientService.findByPatientId(patientId)).thenReturn(patient);
        assertEquals(failure("INVALID_PATIENT_PROVIDER_COMBINATION"), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureWhenAdherenceInputIsInvalid() {
        String patientId = "patientid";
        Dosage dosage = new DosageBuilder(1).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(failure(dosage), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnSuccessWhenAdherenceInputIsValid() {
        String patientId = "patientid";
        Dosage dosage = new DosageBuilder(2).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(success(), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReportAdherenceValidationRequest() {
        AdherenceValidationRequest request = new AdherenceValidationRequest();

        adherenceRecordingOverIVR.validateInput(request, providerId);
        verify(reportingService).reportAdherenceCapture(new AdherenceCaptureReportRequest(request, providerId).captureRequest());
    }
}
