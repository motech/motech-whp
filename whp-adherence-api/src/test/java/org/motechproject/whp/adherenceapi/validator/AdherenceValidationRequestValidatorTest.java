package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.user.builder.ProviderBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

public class AdherenceValidationRequestValidatorTest {


    @Mock
    private AdherenceService adherenceService;
    @Mock
    private AdherenceRequestValidator adherenceRequestValidator;

    private ProviderId providerId;
    private AdherenceValidationRequestValidator adherenceValidationRequestValidator;

    @Before
    public void setUp() {
        initMocks(this);
        initializePatientAndProvider();
        adherenceValidationRequestValidator = new AdherenceValidationRequestValidator(adherenceService, adherenceRequestValidator);
    }

    private void initializePatientAndProvider() {
        providerId = new ProviderId(new ProviderBuilder().withDefaults().withId("providerId").build());
    }

    @Test
    public void shouldReturnSuccessOnValidPatientProviderMappingAndValidDosage() {
        String patientId = "patientid";

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(new DosageBuilder(2).dosage());
        when(adherenceRequestValidator.validatePatientProviderMapping(patientId, providerId)).thenReturn(new ValidationRequestErrors(true, true, true));

        assertEquals(success(), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureOnInValidPatientProviderMappingAndValidDosage() {
        String patientId = "patientid";

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(new DosageBuilder(2).dosage());
        ValidationRequestErrors errors = new ValidationRequestErrors(true, true, false);
        when(adherenceRequestValidator.validatePatientProviderMapping(patientId, providerId)).thenReturn(errors);

        assertEquals(failure(errors.errorMessage()), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureOnValidPatientProviderMappingAndInValidDosage() {
        String patientId = "patientid";
        Dosage dosage = new DosageBuilder(2).dosage();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("8");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        ValidationRequestErrors errors = new ValidationRequestErrors(true, true, true);
        when(adherenceRequestValidator.validatePatientProviderMapping(patientId, providerId)).thenReturn(errors);

        assertEquals(failure(dosage), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }
}
