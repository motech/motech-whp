package org.motechproject.whp.containerregistration.service;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.whp.common.util.WHPDate;
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
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRContainerRegistrationService {

    private final ProviderVerification providerVerification;
    private final ContainerVerification containerVerification;
    private final ContainerRegistrationVerification containerRegistrationVerification;
    private final ReportingPublisherService reportingPublishingService;
    private final ProviderService providerService;

    @Autowired
    public IVRContainerRegistrationService(ProviderVerification providerVerification, ContainerVerification containerVerification, ContainerRegistrationVerification containerRegistrationVerification,
                                           ReportingPublisherService reportingPublishingService, ProviderService providerService) {
        this.providerVerification = providerVerification;
        this.containerVerification = containerVerification;
        this.containerRegistrationVerification = containerRegistrationVerification;
        this.reportingPublishingService = reportingPublishingService;
        this.providerService = providerService;
    }

    public VerificationResult verifyProviderVerificationRequest(ProviderVerificationRequest providerVerificationRequest) {
        VerificationResult verificationResult = providerVerification.verifyRequest(providerVerificationRequest);

        if(verificationResult.isSuccess()){
            ProviderVerificationLogRequest request = getProviderVerificationLogRequest(providerVerificationRequest, verificationResult);
            reportingPublishingService.reportProviderVerificationDetailsLog(request);
        }
        return verificationResult;
    }

    public VerificationResult verifyContainerVerificationRequest(ContainerVerificationRequest containerVerificationRequest) {
        VerificationResult verificationResult = containerVerification.verifyRequest(containerVerificationRequest);

        if(!verificationResult.hasFieldValidationError()){
            ContainerVerificationLogRequest request = getContainerVerificationLogRequest(containerVerificationRequest, verificationResult);
            reportingPublishingService.reportContainerVerificationDetailsLog(request);
        }
        return verificationResult;
    }

    public VerificationResult verifyContainerRegistrationVerificationRequest(IvrContainerRegistrationRequest request) {
        VerificationResult verificationResult = containerRegistrationVerification.verifyRequest(request);
        return verificationResult;
    }

    private ProviderVerificationLogRequest getProviderVerificationLogRequest(ProviderVerificationRequest providerVerificationRequest, VerificationResult verificationResult) {

        ProviderVerificationLogRequest request = new ProviderVerificationLogRequest();
        request.setCallId(providerVerificationRequest.getCall_id());
        request.setMobileNumber(providerVerificationRequest.getPhoneNumber());
        request.setTime(getDateTimeFor(providerVerificationRequest.getTime()));

        if (verificationResult.isSuccess())
            request.setProviderId(providerService.findByMobileNumber(providerVerificationRequest.getPhoneNumber()).getProviderId());

        return request;

    }

    private ContainerVerificationLogRequest getContainerVerificationLogRequest(ContainerVerificationRequest containerVerificationRequest, VerificationResult verificationResult) {
        ContainerVerificationLogRequest request = new ContainerVerificationLogRequest();
        request.setCallId(containerVerificationRequest.getCall_id());
        request.setMobileNumber(containerVerificationRequest.getPhoneNumber());
        request.setValidContainer(verificationResult.isSuccess());

        return request;
    }

    private DateTime getDateTimeFor(String dateTime) {
        return DateTime.parse(dateTime, DateTimeFormat.forPattern(WHPDate.DATE_TIME_FORMAT));
    }
}
