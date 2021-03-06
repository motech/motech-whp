package org.motechproject.whp.adherenceapi.adherence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCallStatusReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceCallStatusValidationResponse;
import org.motechproject.whp.adherenceapi.validator.AdherenceCallStatusRequestValidator;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceCallStatusOverIVRTest {


    @Mock
    private AdherenceCallStatusRequestValidator adherenceCallStatusRequestValidator;
    @Mock
    private ReportingPublisherService reportingService;
    private AdherenceCallStatusOverIVR adherenceCallStatusOverIVR;
    private ProviderId providerId;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceCallStatusOverIVR = new AdherenceCallStatusOverIVR(reportingService, adherenceCallStatusRequestValidator);
    }

    @Test
    public void shouldReportAndReturnSuccessOnAdherenceCallStatusOperation() {
        AdherenceCallStatusRequest adherenceCallStatusRequest = new AdherenceCallStatusRequest();
        adherenceCallStatusRequest.setStartTime("10/12/2012 12:32:35");
        adherenceCallStatusRequest.setEndTime("21/12/2012 12:32:35");
        adherenceCallStatusRequest.setAttemptTime("21/12/2012 12:32:35");
        adherenceCallStatusRequest.setPatientCount("2");
        adherenceCallStatusRequest.setAdherenceCapturedCount("3");
        adherenceCallStatusRequest.setAdherenceNotCapturedCount("1");

        when(adherenceCallStatusRequestValidator.validate(adherenceCallStatusRequest)).thenReturn(AdherenceCallStatusValidationResponse.success());

        assertEquals(AdherenceCallStatusValidationResponse.success(), adherenceCallStatusOverIVR.recordCallStatus(adherenceCallStatusRequest));
        verify(reportingService).reportCallStatus(new AdherenceCallStatusReportRequest(adherenceCallStatusRequest).callStatusRequest());
    }

    @Test
    public void shouldReturnFailureOnAdherenceCallStatusOperationForValidationFailures() {
        AdherenceCallStatusRequest adherenceCallStatusRequest = new AdherenceCallStatusRequest();

        when(adherenceCallStatusRequestValidator.validate(adherenceCallStatusRequest)).thenReturn(AdherenceCallStatusValidationResponse.failure("error"));

        assertEquals(AdherenceCallStatusValidationResponse.failure("error"), adherenceCallStatusOverIVR.recordCallStatus(adherenceCallStatusRequest));
        verify(reportingService, never()).reportCallLog(any(AdherenceCallLogRequest.class));
    }
}
