package org.motechproject.whp.adherenceapi.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceRequestsValidator;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
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

    @Before
    public void setUp() {
        initMocks(this);
        adherenceWebService = new AdherenceWebService(adherenceService, reportingPublishingService, adherenceRequestsValidator, providerService);
    }

    @Test
    public void shouldReturnAValidFlashingResponseAndReportIt() {
        LocalDate today = DateUtil.today();
        String providerId = "raj";
        String msisdn = "1234567890";

        AdherenceCaptureFlashingRequest adherenceCaptureFlashingRequest = new AdherenceCaptureFlashingRequest();
        adherenceCaptureFlashingRequest.setMsisdn(msisdn);
        adherenceCaptureFlashingRequest.setCallTime("14/08/2012 11:20:59");
        when(adherenceRequestsValidator.validateFlashingRequest(adherenceCaptureFlashingRequest, today)).thenReturn(null);
        when(providerService.findByMobileNumber(msisdn)).thenReturn(new Provider(providerId, msisdn, null, null));

        List<String> patientsWithAdherence = asList("1234", "5678");
        List<Patient> patientsForProvider = asList(patients("1234"), patients("5678"), patients("9012"));
        AdherenceSummary adherenceSummary = new AdherenceSummary(patientsWithAdherence, patientsForProvider);
        when(adherenceService.adherenceSummary(providerId, today)).thenReturn(adherenceSummary);

        AdherenceCaptureFlashingResponse flashingResponse = adherenceWebService.processFlashingRequest(adherenceCaptureFlashingRequest, today);

        assertEquals(new AdherenceCaptureFlashingResponse(patientsWithAdherence, asList("1234", "5678", "9012")), flashingResponse);

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

        AdherenceCaptureFlashingRequest adherenceCaptureFlashingRequest = new AdherenceCaptureFlashingRequest();
        adherenceCaptureFlashingRequest.setMsisdn(msisdn);
        when(adherenceRequestsValidator.validateFlashingRequest(adherenceCaptureFlashingRequest, today)).thenReturn(new ErrorWithParameters("first_code"));

        AdherenceCaptureFlashingResponse flashingResponse = adherenceWebService.processFlashingRequest(adherenceCaptureFlashingRequest, today);

        assertEquals(AdherenceCaptureFlashingResponse.failureResponse("first_code"), flashingResponse);
        verify(adherenceService, never()).adherenceSummary(anyString(), any(LocalDate.class));
        verify(providerService, never()).findByMobileNumber(anyString());
    }

    private Patient patients(String patientId) {
        return new PatientBuilder().withDefaults().withPatientId(patientId).build();
    }
}
