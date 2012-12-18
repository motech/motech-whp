package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceRequestValidatorTest {
    private AdherenceRequestValidator adherenceRequestValidator;

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private PatientService patientService;
    private ProviderId providerId;

    @Before
    public void setUp() {
        initMocks(this);
        initializeProvider();
        adherenceRequestValidator = new AdherenceRequestValidator(adherenceService, patientService);
    }

    private void initializeProvider() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
    }

    @Test
    public void shouldReturnFailureWhenUnableToFindDosageForPatient() {
        String invalidPatientId = "patientid";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(invalidPatientId);
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(invalidPatientId)).thenReturn(null);
        assertEquals("INVALID_PATIENT", adherenceRequestValidator.validatePatientProviderMapping(invalidPatientId, providerId).errorMessage());
    }

    @Test
    public void shouldReturnFailureWhenPhoneNumberDoesNotBelongToAnyProvider() {
        ProviderId emptyProviderId = new ProviderId();
        assertEquals("INVALID_MOBILE_NUMBER", adherenceRequestValidator.validatePatientProviderMapping("patientId", emptyProviderId).errorMessage());
    }

    @Test
    public void shouldReturnFailureWhenPatientDoesNotBelongToProvider() {
        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withProviderId("someOtherProviderId").build();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);

        when(adherenceService.dosageForPatient(patientId)).thenReturn(new DosageBuilder(1).dosage());
        when(patientService.findByPatientId(patientId)).thenReturn(patient);
        assertEquals("INVALID_PATIENT_PROVIDER_COMBINATION", adherenceRequestValidator.validatePatientProviderMapping(patientId, providerId).errorMessage());
    }
}
