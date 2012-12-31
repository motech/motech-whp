package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceConfirmationRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceValidationRequestValidator;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceConfirmationOverIVRTest {

    private AdherenceConfirmationOverIVR adherenceConfirmationOverIVR;

    @Mock
    private ReportingPublisherService reportingService;
    @Mock
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    private AdherenceValidationRequestValidator adherenceValidationRequestValidator;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceConfirmationOverIVR = new AdherenceConfirmationOverIVR(reportingService, treatmentUpdateOrchestrator, adherenceValidationRequestValidator);
    }

    @Test
    public void shouldDoNothingAndReturnAFailureResponseWhenThereAreValidationErrors() {
        String callId = "1234567890";
        AdherenceConfirmationRequest confirmationRequest = new AdherenceConfirmationRequest();
        confirmationRequest.setCallId(callId);

        ProviderId providerId = mock(ProviderId.class);
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setCallId(callId);

        AdherenceValidationResponse validationResponse = AdherenceValidationResponse.failure();
        when(adherenceValidationRequestValidator.validate(validationRequest, providerId)).thenReturn(validationResponse);

        AdherenceValidationResponse response = adherenceConfirmationOverIVR.confirmAdherence(confirmationRequest, providerId);

        assertEquals(validationResponse, response);
        verify(reportingService, never()).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
        verify(treatmentUpdateOrchestrator, never()).recordWeeklyAdherence(any(WeeklyAdherenceSummary.class), anyString(), any(AuditParams.class));
    }

    @Test
    public void shouldReturnASuccessfulResponseWithReporting() {
        String callId = "callId";
        String doseTakenCount = "3";
        String ivrFileLength = "20";

        AdherenceConfirmationRequest confirmationRequest = new AdherenceConfirmationRequest();
        confirmationRequest.setCallId(callId);
        confirmationRequest.setIvrFileLength(ivrFileLength);
        confirmationRequest.setDoseTakenCount(doseTakenCount);

        ProviderId providerId = mock(ProviderId.class);
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setCallId(callId);
        validationRequest.setIvrFileLength(ivrFileLength);
        validationRequest.setDoseTakenCount(doseTakenCount);

        AdherenceValidationResponse validationResponse = AdherenceValidationResponse.success();
        when(adherenceValidationRequestValidator.validate(validationRequest, providerId)).thenReturn(validationResponse);

        AdherenceValidationResponse response = adherenceConfirmationOverIVR.confirmAdherence(confirmationRequest, providerId);

        assertEquals(validationResponse, response);
        verify(reportingService).reportAdherenceCapture(new AdherenceCaptureReportRequest(validationRequest, providerId, true, AdherenceCaptureStatus.ADHERENCE_PROVIDED).request());
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(confirmationRequest.getPatientId(), currentAdherenceCaptureWeek(), Integer.parseInt(doseTakenCount));
        verify(treatmentUpdateOrchestrator).recordWeeklyAdherence(weeklyAdherenceSummary, confirmationRequest.getPatientId(), new AuditParams(providerId.value(), AdherenceSource.IVR, ""));
    }
}
