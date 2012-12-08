package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.FlashingLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse.failureResponse;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.INVALID_MOBILE_NUMBER;
import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.NON_ADHERENCE_DAY;

public class AdherenceSummaryOverIVRTest extends BaseUnitTest {

    AdherenceSummaryOverIVR adherenceSummaryOverIVR;

    @Mock
    private AdherenceService adherenceService;
    @Mock
    private AdherenceWindow adherenceWindow;
    @Mock
    private ReportingPublisherService reportingPublishingService;

    String msisdn = "1234567890";
    String flashingCallId = "callId";
    String providerId = "providerid";
    String callTime = "14/08/2012 11:20:59";
    Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(today());
        adherenceSummaryOverIVR = new AdherenceSummaryOverIVR(adherenceService, adherenceWindow, reportingPublishingService);
    }

    @Test
    public void shouldGenerateResponseAndReportFlashingForValidRequest() {
        AdherenceFlashingRequest adherenceFlashingRequest = createFlashingRequest(msisdn, flashingCallId, callTime);

        List<String> patientIdsWithAdherence = asList("patient1", "patient2");
        List<String> patientIdsWithoutAdherence = asList("patient3");
        List<Patient> patientsWithoutAdherence = asList(new PatientBuilder().withDefaults().withPatientId("patient3").build());

        when(adherenceWindow.isValidAdherenceDay(today())).thenReturn(true);
        when(adherenceService.adherenceSummary(providerId, today())).thenReturn(new AdherenceSummary(patientIdsWithAdherence, patientsWithoutAdherence));

        FlashingLogRequest expectedFlashingLogRequest = expectedFlashingRequest(msisdn, flashingCallId, providerId, callTime);
        AdherenceFlashingResponse expectedResponse = new AdherenceFlashingResponse(patientIdsWithAdherence, patientIdsWithoutAdherence);

        AdherenceFlashingResponse response = adherenceSummaryOverIVR.value(adherenceFlashingRequest, new ProviderId(provider));

        assertThat(response, is(expectedResponse));
        verify(reportingPublishingService).reportFlashingRequest(expectedFlashingLogRequest);
        verify(adherenceWindow).isValidAdherenceDay(today());
    }

    @Test
    public void shouldGenerateErrorResponseAndReportFlashingForInvaldMSISDN() {
        AdherenceFlashingRequest adherenceFlashingRequest = createFlashingRequest(msisdn, flashingCallId, callTime);

        FlashingLogRequest expectedFlashingLogRequest = expectedFlashingRequest(msisdn, flashingCallId, null, callTime);
        AdherenceFlashingResponse expectedResponse = failureResponse(INVALID_MOBILE_NUMBER.name());

        AdherenceFlashingResponse response = adherenceSummaryOverIVR.value(adherenceFlashingRequest, new ProviderId(null));

        assertThat(response, is(expectedResponse));
        verify(reportingPublishingService).reportFlashingRequest(expectedFlashingLogRequest);
        verifyZeroInteractions(adherenceWindow);
    }

    @Test
    public void shouldGenerateErrorResponseAndReportFlashingForNonAdherenceDay() {
        AdherenceFlashingRequest adherenceFlashingRequest = createFlashingRequest(msisdn, flashingCallId, callTime);

        FlashingLogRequest expectedFlashingLogRequest = expectedFlashingRequest(msisdn, flashingCallId, providerId, callTime);
        AdherenceFlashingResponse expectedResponse = failureResponse(NON_ADHERENCE_DAY.name());

        AdherenceFlashingResponse response = adherenceSummaryOverIVR.value(adherenceFlashingRequest, new ProviderId(provider));

        assertThat(response, is(expectedResponse));
        verify(reportingPublishingService).reportFlashingRequest(expectedFlashingLogRequest);
        verify(adherenceWindow).isValidAdherenceDay(today());
    }

    private AdherenceFlashingRequest createFlashingRequest(String msisdn, String flashingCallId, String callTime) {
        AdherenceFlashingRequest adherenceFlashingRequest = new AdherenceFlashingRequest();
        adherenceFlashingRequest.setMsisdn(msisdn);
        adherenceFlashingRequest.setCallId(flashingCallId);
        adherenceFlashingRequest.setCallTime(callTime);
        return adherenceFlashingRequest;
    }

    private FlashingLogRequest expectedFlashingRequest(String msisdn, String flashingCallId, String providerId, String callTime) {
        FlashingLogRequest flashingLogRequest = new FlashingLogRequest();
        flashingLogRequest.setProviderId(providerId);
        flashingLogRequest.setCallTime(WHPDateTime.date(callTime).date().toDate());
        flashingLogRequest.setCreationTime(today().toDate());
        flashingLogRequest.setMobileNumber(msisdn);
        flashingLogRequest.setFlashingCallId(flashingCallId);
        return flashingLogRequest;
    }

}
