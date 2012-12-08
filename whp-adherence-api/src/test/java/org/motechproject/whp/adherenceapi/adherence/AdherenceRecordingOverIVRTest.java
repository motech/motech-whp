package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.service.AdherenceService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

public class AdherenceRecordingOverIVRTest {

    @Mock
    private AdherenceService adherenceService;

    private AdherenceRecordingOverIVR adherenceRecordingOverIVR;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceRecordingOverIVR = new AdherenceRecordingOverIVR(adherenceService);
    }

    @Test
    public void shouldReturnFailureWhenUnableToFindDosageForPatient() {
        String invalidPatientId = "patientId";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(invalidPatientId);

        when(adherenceService.dosageForPatient(invalidPatientId)).thenReturn(null);
        assertEquals(failure(), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest));
    }

    @Test
    public void shouldReturnFailureWhenAdherenceInputIsInvalid() {
        String patientId = "patientId";
        Dosage dosage = new DosageBuilder(1).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(failure(dosage), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest));
    }

    @Test
    public void shouldReturnSuccessWhenAdherenceInputIsValid() {
        String patientId = "patientId";
        Dosage dosage = new DosageBuilder(2).dosage();
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount("2");

        when(adherenceService.dosageForPatient(patientId)).thenReturn(dosage);
        assertEquals(success(), adherenceRecordingOverIVR.validateInput(adherenceValidationRequest));
    }
}
