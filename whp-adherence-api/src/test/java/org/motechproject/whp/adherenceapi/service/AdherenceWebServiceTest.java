package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.TreatmentCategoryInfo;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponseBuilder;
import org.motechproject.whp.adherenceapi.validator.AdherenceRequestsValidator;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.FlashingLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceWebServiceTest {

    private AdherenceWebService adherenceWebService;
    @Mock
    private AdherenceService adherenceService;
    @Mock
    private ReportingPublisherService reportingPublishingService;
    @Mock
    private AdherenceRequestsValidator adherenceRequestsValidator;
    @Mock
    private ProviderService providerService;
    @Mock
    private AdherenceValidationResponseBuilder adherenceValidationResponseBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceWebService = new AdherenceWebService(adherenceService, reportingPublishingService, adherenceRequestsValidator, providerService, adherenceValidationResponseBuilder);
    }

    @Test
    public void shouldReturnAValidFlashingResponseAndReportIt() {
        LocalDate today = DateUtil.today();
        String providerId = "raj";
        String msisdn = "1234567890";

        AdherenceFlashingRequest adherenceFlashingRequest = new AdherenceFlashingRequest();
        adherenceFlashingRequest.setMsisdn(msisdn);
        adherenceFlashingRequest.setCallTime("14/08/2012 11:20:59");
        when(adherenceRequestsValidator.validateFlashingRequest(adherenceFlashingRequest, today)).thenReturn(null);
        when(providerService.findByMobileNumber(msisdn)).thenReturn(new Provider(providerId, msisdn, null, null));

        List<String> patientsWithAdherence = asList("1234", "5678");
        List<Patient> patientsForProvider = asList(patients("1234"), patients("5678"), patients("9012"));
        AdherenceSummary adherenceSummary = new AdherenceSummary(patientsWithAdherence, patientsForProvider);
        when(adherenceService.adherenceSummary(providerId, today)).thenReturn(adherenceSummary);

        AdherenceFlashingResponse flashingResponse = adherenceWebService.processFlashingRequest(adherenceFlashingRequest, today);

        assertEquals(new AdherenceFlashingResponse(patientsWithAdherence, asList("1234", "5678", "9012")), flashingResponse);

        verify(adherenceService).adherenceSummary(providerId, today);
        verify(providerService).findByMobileNumber(msisdn);
        ArgumentCaptor<FlashingLogRequest> captor = ArgumentCaptor.forClass(FlashingLogRequest.class);
        verify(reportingPublishingService).reportFlashingRequest(captor.capture());

        FlashingLogRequest flashingLogRequest = captor.getValue();
        assertThat(flashingLogRequest.getMobileNumber(), is(msisdn));
        assertThat(flashingLogRequest.getProviderId(), is(providerId));
        assertThat(flashingLogRequest.getCallTime(), is(new WHPDateTime("14/08/2012 11:20:59").date().toDate()));
        assertNotNull(flashingLogRequest.getCreationTime());
    }

    @Test
    public void shouldReturnAFailureFlashingResponseIfValidationFails() {
        LocalDate today = DateUtil.today();
        String msisdn = "1234567890";

        AdherenceFlashingRequest adherenceFlashingRequest = new AdherenceFlashingRequest();
        adherenceFlashingRequest.setMsisdn(msisdn);
        when(adherenceRequestsValidator.validateFlashingRequest(adherenceFlashingRequest, today)).thenReturn(new ErrorWithParameters("first_code"));

        AdherenceFlashingResponse flashingResponse = adherenceWebService.processFlashingRequest(adherenceFlashingRequest, today);

        assertEquals(AdherenceFlashingResponse.failureResponse("first_code"), flashingResponse);
        verify(adherenceService, never()).adherenceSummary(anyString(), any(LocalDate.class));
        verify(providerService, never()).findByMobileNumber(anyString());
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
