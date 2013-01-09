package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceNotCapturedRequestValidatorTest {

    AdherenceNotCapturedRequestValidator adherenceNotCapturedRequestValidator;

    @Mock
    private AdherenceRequestValidator adherenceRequestValidator;
    private ProviderId providerId;

    @Before
    public void setup() {
        initMocks(this);
        adherenceNotCapturedRequestValidator = new AdherenceNotCapturedRequestValidator(adherenceRequestValidator);
        initializeProvider();
    }

    private void initializeProvider() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
    }

    @Test
    public void shouldReturnSuccessOnValidPatientProviderMapping() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        String patientId = "patientId";
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        ProviderId providerID = new ProviderId();
        when(adherenceRequestValidator.validatePatientProviderMapping(patientId, providerID)).thenReturn(new ValidationRequestErrors(true, true, true));

        assertEquals(new AdherenceValidationResponse().success(), adherenceNotCapturedRequestValidator.validate(adherenceValidationRequest, providerID));
    }

    @Test
    public void shouldReturnFailureOnInvalidPatientProviderMapping() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        String patientId = "patientId";
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        ProviderId providerID = new ProviderId();
        when(adherenceRequestValidator.validatePatientProviderMapping(patientId, providerID)).thenReturn(new ValidationRequestErrors(true, true, false));

        assertEquals(new AdherenceValidationResponse().failure("INVALID_PATIENT_PROVIDER_COMBINATION"), adherenceNotCapturedRequestValidator.validate(adherenceValidationRequest, providerID));
    }
}
