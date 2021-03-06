package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.*;

public class AdherenceRequestValidatorTest {

    private AdherenceRequestValidator adherenceRequestValidator;

    @Mock
    private ProviderService providerService;
    private ProviderId providerId;

    @Before
    public void setUp() {
        initMocks(this);
        initializeProvider();
        adherenceRequestValidator = new AdherenceRequestValidator(providerService);
    }

    private void initializeProvider() {
        providerId = new ProviderId("providerId");
    }

    @Test
    public void shouldReturnFailureWhenUnableToFindDosageForPatient() {
        String invalidPatientId = "patientid";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(invalidPatientId);
        adherenceValidationRequest.setTimeTaken("1000");

        assertEquals(INVALID_PATIENT.name(), adherenceRequestValidator.validatePatientProviderMapping(providerId, null).errorMessage());
    }

    @Test
    public void shouldReturnFailureWhenThereIsNoValidProviderForTheGivenProviderId() {
        ProviderId emptyProviderId = new ProviderId();
        assertEquals(INVALID_PROVIDER.name(), adherenceRequestValidator.validatePatientProviderMapping(emptyProviderId, null).errorMessage());
    }

    @Test
    public void shouldReturnFailureWhenPatientDoesNotBelongToProvider() {
        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withProviderId("someOtherProviderId").build();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);

        assertEquals(INVALID_PATIENT_PROVIDER_COMBINATION.name(), adherenceRequestValidator.validatePatientProviderMapping(providerId, patient).errorMessage());
    }

    @Test
    public void shouldReturnFailureIfProviderIdIsUnknown() {
        String providerId = "providerid";
        when(providerService.findByProviderId(providerId)).thenReturn(null);
        assertEquals(INVALID_PROVIDER.name(), adherenceRequestValidator.validateProvider(providerId).errorMessage());
    }

    @Test
    public void shouldReturnSuccessIfProviderIdIsRegistered(){
        String providerId = "providerid";
        when(providerService.findByProviderId(providerId)).thenReturn(new Provider(providerId, null, null, null));
        assertEquals("", adherenceRequestValidator.validateProvider(providerId).errorMessage());
    }
}
