package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceValidationRequestValidatorTest {


    @Mock
    private AdherenceService adherenceService;
    @Mock
    private AdherenceRequestValidator adherenceRequestValidator;
    @Mock
    private PatientService patientService;
    @Mock
    private Patient patient;

    private ProviderId providerId;
    private AdherenceValidationRequestValidator adherenceValidationRequestValidator;
    private final String patientId = "patientid";

    @Before
    public void setUp() {
        initMocks(this);
        providerId = new ProviderId("providerId");
        adherenceValidationRequestValidator = new AdherenceValidationRequestValidator(adherenceService, adherenceRequestValidator, patientService);
        when(patientService.findByPatientId(patientId)).thenReturn(patient);
    }

    @Test
    public void shouldReturnSuccessOnValidPatientProviderMappingAndValidDosage() {
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        Dosage dosage = new DosageBuilder(2).dosage();
        when(adherenceService.dosageForPatient(patient)).thenReturn(dosage);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(new ValidationRequestErrors(true, true, true));

        assertEquals(new AdherenceValidationResponse(dosage).success(), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureOnInValidPatientProviderMappingAndValidDosage() {

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");
        adherenceValidationRequest.setTimeTaken("1000");

        Dosage dosage = new DosageBuilder(2).dosage();
        when(adherenceService.dosageForPatient(patient)).thenReturn(dosage);
        ValidationRequestErrors errors = new ValidationRequestErrors(true, true, false);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(errors);

        assertEquals(new AdherenceValidationResponse(dosage).failure(errors.errorMessage()), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureOnValidPatientProviderMappingAndInValidDosage() {
        Dosage dosage = new DosageBuilder(2).dosage();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("8");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patient)).thenReturn(dosage);
        ValidationRequestErrors errors = new ValidationRequestErrors(true, true, true);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(errors);

        assertEquals(new AdherenceValidationResponse(dosage).invalidAdherenceRange(), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureOnValidPatientProviderMappingAndNonNumericInValidDosage() {
        Dosage dosage = new DosageBuilder(2).dosage();

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("*");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patient)).thenReturn(dosage);
        ValidationRequestErrors errors = new ValidationRequestErrors(true, true, true);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(errors);

        assertEquals(new AdherenceValidationResponse(dosage).invalidAdherenceRange(), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }

    @Test
    public void shouldReturnFailureInValidPatient() {

        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("8");
        adherenceValidationRequest.setTimeTaken("1000");

        when(adherenceService.dosageForPatient(patient)).thenReturn(null);
        ValidationRequestErrors errors = new ValidationRequestErrors(true, false, false);
        when(adherenceRequestValidator.validatePatientProviderMapping(providerId, patient)).thenReturn(errors);

        assertEquals(new AdherenceValidationResponse(null).failure(errors.errorMessage()), adherenceValidationRequestValidator.validate(adherenceValidationRequest, providerId));
    }
}
