package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceNotCapturedRequestValidatorTest {

    AdherenceNotCapturedRequestValidator adherenceNotCapturedRequestValidator;

    @Mock
    private AdherenceRequestValidator adherenceRequestValidator;
    @Mock
    private PatientService patientService;
    @Mock
    private Patient patient;
    private ProviderId providerId;
    private final String patientId = "patientId";

    @Before
    public void setup() {
        initMocks(this);
        adherenceNotCapturedRequestValidator = new AdherenceNotCapturedRequestValidator(adherenceRequestValidator, patientService);
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
        when(patientService.findByPatientId(patientId)).thenReturn(patient);
    }

    @Test
    public void shouldReturnSuccessOnValidPatientProviderMapping() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        ProviderId providerID = new ProviderId();
        when(adherenceRequestValidator.validatePatientProviderMapping(providerID, patient)).thenReturn(new ValidationRequestErrors(true, true, true));

        assertEquals(new AdherenceValidationResponse().success(), adherenceNotCapturedRequestValidator.validate(adherenceValidationRequest, providerID));
    }

    @Test
    public void shouldReturnFailureOnInvalidPatientProviderMapping() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        ProviderId providerId = mock(ProviderId.class);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(new ValidationRequestErrors(true, true, false));

        assertEquals(new AdherenceValidationResponse().failure("INVALID_PATIENT_PROVIDER_COMBINATION"), adherenceNotCapturedRequestValidator.validate(adherenceValidationRequest, providerId));
    }
}
