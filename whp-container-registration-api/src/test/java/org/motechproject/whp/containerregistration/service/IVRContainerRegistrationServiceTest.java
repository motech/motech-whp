package org.motechproject.whp.containerregistration.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerVerificationLogRequest;
import org.motechproject.whp.reports.contract.ProviderVerificationLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class IVRContainerRegistrationServiceTest {

    private IVRContainerRegistrationService ivrContainerRegistrationService;

    @Mock
    private ProviderVerification providerVerification;
    @Mock
    private ContainerVerification containerVerification;
    @Mock
    private ContainerRegistrationVerification containerRegistrationVerification;
    @Mock
    private ReportingPublisherService reportingPublishingService;
    @Mock
    private ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        ivrContainerRegistrationService = new IVRContainerRegistrationService(providerVerification, containerVerification, containerRegistrationVerification, reportingPublishingService, providerService);
    }

    @Test
    public void shouldReportProviderVerificationRequestForSuccessfulVerificationResult() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("29/11/1986 20:20:20");

        when(providerVerification.verifyRequest(request)).thenReturn(new VerificationResult());
        Provider existingProvider = new Provider();
        existingProvider.setProviderId("raj");
        when(providerService.findByMobileNumber(msisdn)).thenReturn(existingProvider);

        ivrContainerRegistrationService.verifyProviderVerificationRequest(request);

        verify(providerVerification).verifyRequest(request);
        verify(providerService).findByMobileNumber(msisdn);
        ArgumentCaptor<ProviderVerificationLogRequest> captor = ArgumentCaptor.forClass(ProviderVerificationLogRequest.class);
        verify(reportingPublishingService).reportProviderVerificationDetailsLog(captor.capture());
        ProviderVerificationLogRequest actualLogRequest = captor.getValue();

        assertEquals(request.getCall_id(), actualLogRequest.getCallId());
        assertEquals(request.getMsisdn(), actualLogRequest.getMobileNumber());
        assertNotNull(actualLogRequest.getTime());
        assertEquals(existingProvider.getProviderId(), actualLogRequest.getProviderId());
    }

    @Test
    public void shouldNotReportProviderVerificationRequestWithFieldValidationFailure() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("29/11/1986 20:20:20");

        VerificationResult verificationResult = new VerificationResult(new WHPError(WHPErrorCode.FIELD_VALIDATION_FAILED));
        when(providerVerification.verifyRequest(request)).thenReturn(verificationResult);

        ivrContainerRegistrationService.verifyProviderVerificationRequest(request);

        verify(providerVerification).verifyRequest(request);
        verify(reportingPublishingService, never()).reportProviderVerificationDetailsLog(any(ProviderVerificationLogRequest.class));
    }

    @Test
    public void shouldNotReportProviderVerificationRequestWithAnyValidationFailure() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("29/11/1986 20:20:20");

        when(providerVerification.verifyRequest(request)).thenReturn(new VerificationResult(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER)));

        ivrContainerRegistrationService.verifyProviderVerificationRequest(request);

        verify(providerVerification).verifyRequest(request);
        verify(reportingPublishingService, never()).reportProviderVerificationDetailsLog(any(ProviderVerificationLogRequest.class));
    }

    @Test
    public void shouldReportContainerVerificationRequestForSuccessfulVerificationResult() {
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest();
        containerVerificationRequest.setCall_id("callId");
        containerVerificationRequest.setMsisdn("1234567890");
        containerVerificationRequest.setContainer_id("something");
        when(containerVerification.verifyRequest(containerVerificationRequest)).thenReturn(new VerificationResult());

        ivrContainerRegistrationService.verifyContainerVerificationRequest(containerVerificationRequest);

        verify(containerVerification).verifyRequest(containerVerificationRequest);
        ArgumentCaptor<ContainerVerificationLogRequest> captor = ArgumentCaptor.forClass(ContainerVerificationLogRequest.class);
        verify(reportingPublishingService).reportContainerVerificationDetailsLog(captor.capture());
        ContainerVerificationLogRequest actualLogRequest = captor.getValue();

        assertEquals(containerVerificationRequest.getCall_id(), actualLogRequest.getCallId());
        assertEquals(containerVerificationRequest.getPhoneNumber(), actualLogRequest.getMobileNumber());
        assertTrue(actualLogRequest.isValidContainer());
    }

    @Test
    public void shouldNotReportContainerVerificationRequestForRequestLevelFailedVerificationResult() {
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest();
        containerVerificationRequest.setCall_id("callId");
        containerVerificationRequest.setMsisdn("1234567890");
        containerVerificationRequest.setContainer_id("something");
        when(containerVerification.verifyRequest(containerVerificationRequest)).thenReturn(new VerificationResult(new WHPError(WHPErrorCode.FIELD_VALIDATION_FAILED)));

        ivrContainerRegistrationService.verifyContainerVerificationRequest(containerVerificationRequest);

        verify(containerVerification).verifyRequest(containerVerificationRequest);

        verify(reportingPublishingService, never()).reportContainerVerificationDetailsLog(any(ContainerVerificationLogRequest.class));
    }

    @Test
    public void shouldReportContainerVerificationRequestForDomainLevelFailedVerificationResult() {
        ContainerVerificationRequest containerVerificationRequest = new ContainerVerificationRequest();
        containerVerificationRequest.setCall_id("callId");
        containerVerificationRequest.setMsisdn("1234567890");
        containerVerificationRequest.setContainer_id("something");
        when(containerVerification.verifyRequest(containerVerificationRequest)).thenReturn(new VerificationResult(new WHPError(WHPErrorCode.INVALID_CONTAINER_ID)));

        ivrContainerRegistrationService.verifyContainerVerificationRequest(containerVerificationRequest);

        verify(containerVerification).verifyRequest(containerVerificationRequest);
        ArgumentCaptor<ContainerVerificationLogRequest> captor = ArgumentCaptor.forClass(ContainerVerificationLogRequest.class);
        verify(reportingPublishingService).reportContainerVerificationDetailsLog(captor.capture());
        ContainerVerificationLogRequest actualLogRequest = captor.getValue();

        assertEquals(containerVerificationRequest.getCall_id(), actualLogRequest.getCallId());
        assertEquals(containerVerificationRequest.getPhoneNumber(), actualLogRequest.getMobileNumber());
        assertFalse(actualLogRequest.isValidContainer());
    }

    @Test
    public void shouldVerifyContainerRegistrationVerificationRequest() {
        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest();

        ivrContainerRegistrationService.verifyContainerRegistrationVerificationRequest(request);

        verify(containerRegistrationVerification).verifyRequest(request);
    }
}
