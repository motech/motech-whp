package org.motechproject.whp.adherenceapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponseBuilder;
import org.motechproject.whp.adherenceapi.validator.AdherenceRequestsValidator;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceWebServiceTest {

    private AdherenceWebService adherenceWebService;

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private AdherenceRequestsValidator adherenceRequestsValidator;
    @Mock
    private AdherenceValidationResponseBuilder adherenceValidationResponseBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceWebService = new AdherenceWebService(adherenceService, adherenceRequestsValidator, adherenceValidationResponseBuilder);
    }

    @Test
    public void shouldReturnSuccessfulValidationResponse() {
        String patientId = "1234";
        String doseTakenCount = "3";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount(doseTakenCount);

        when(adherenceService.validateDosage(patientId, doseTakenCount)).thenReturn(true);
        AdherenceValidationResponse successfulResponse = new AdherenceValidationResponse();
        when(adherenceValidationResponseBuilder.successfulResponse()).thenReturn(successfulResponse);

        AdherenceValidationResponse adherenceValidationResponse = adherenceWebService.processValidationRequest(adherenceValidationRequest);

        assertEquals(successfulResponse, adherenceValidationResponse);
        verify(adherenceService).validateDosage(patientId, doseTakenCount);
        verify(adherenceValidationResponseBuilder).successfulResponse();
    }

    @Test
    public void shouldReturnFailureValidationResponseForInvalidDosage() {
        String patientId = "1234";
        String invalidDoseTakenCount = "7";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount(invalidDoseTakenCount);

        when(adherenceService.validateDosage(patientId, invalidDoseTakenCount)).thenReturn(false);

        AdherenceValidationResponse failureResponse = new AdherenceValidationResponse();
        TreatmentCategoryInfo treatmentCategoryInfo = new TreatmentCategoryInfo(new TreatmentCategory());
        when(adherenceService.getTreatmentCategoryInformation(patientId)).thenReturn(treatmentCategoryInfo);
        when(adherenceValidationResponseBuilder.invalidDosageFailureResponse(treatmentCategoryInfo)).thenReturn(failureResponse);

        AdherenceValidationResponse adherenceValidationResponse = adherenceWebService.processValidationRequest(adherenceValidationRequest);

        assertEquals(failureResponse, adherenceValidationResponse);
        verify(adherenceService).validateDosage(patientId, invalidDoseTakenCount);
        verify(adherenceService).getTreatmentCategoryInformation(patientId);
        verify(adherenceValidationResponseBuilder).invalidDosageFailureResponse(treatmentCategoryInfo);
    }

    @Test
    public void shouldReturnFailureValidationResponseForValidationFailures() {
        String patientId = "1234";
        String invalidDoseTakenCount = "7";
        AdherenceValidationRequest adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId(patientId);
        adherenceValidationRequest.setDoseTakenCount(invalidDoseTakenCount);

        when(adherenceRequestsValidator.validateValidationRequest(adherenceValidationRequest)).thenReturn(new ErrorWithParameters("some error code"));
        AdherenceValidationResponse failureResponse = new AdherenceValidationResponse();
        when(adherenceValidationResponseBuilder.validationFailureResponse()).thenReturn(failureResponse);

        AdherenceValidationResponse adherenceValidationResponse = adherenceWebService.processValidationRequest(adherenceValidationRequest);

        assertEquals(failureResponse, adherenceValidationResponse);
        verify(adherenceRequestsValidator).validateValidationRequest(adherenceValidationRequest);
        verify(adherenceValidationResponseBuilder).validationFailureResponse();
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
