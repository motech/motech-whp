package org.motechproject.whp.containerregistration.service;

import freemarker.template.TemplateException;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;
import org.motechproject.whp.containerregistration.mapper.ContainerRegistrationRequestMapper;
import org.motechproject.whp.containerregistration.reporting.ContainerVerificationReportRequest;
import org.motechproject.whp.containerregistration.reporting.ProviderVerificationReportingRequest;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.ContainerVerificationLogRequest;
import org.motechproject.whp.reports.contract.ProviderVerificationLogRequest;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IVRContainerRegistrationService {

    private final ProviderVerification providerVerification;
    private final ContainerVerification containerVerification;
    private final ContainerRegistrationVerification containerRegistrationVerification;
    private final ReportingPublisherService reportingPublishingService;
    private final ProviderService providerService;
    private final ContainerService containerService;

    @Autowired
    public IVRContainerRegistrationService(ProviderVerification providerVerification,
                                           ContainerVerification containerVerification,
                                           ContainerRegistrationVerification containerRegistrationVerification,
                                           ReportingPublisherService reportingPublishingService,
                                           ProviderService providerService,
                                           ContainerService containerService) {
        this.providerVerification = providerVerification;
        this.containerVerification = containerVerification;
        this.containerRegistrationVerification = containerRegistrationVerification;
        this.reportingPublishingService = reportingPublishingService;
        this.providerService = providerService;
        this.containerService = containerService;
    }

    public VerificationResult verify(ProviderVerificationRequest providerVerificationRequest) {
        VerificationResult verificationResult = providerVerification.verifyRequest(providerVerificationRequest);
        if (verificationResult.isReportable()) {
            ProviderVerificationReportingRequest reportingRequest = new ProviderVerificationReportingRequest(
                    providerVerificationRequest,
                    providerId(
                            providerVerificationRequest.getPhoneNumber(),
                            verificationResult.isSuccess()
                    )
            );
            ProviderVerificationLogRequest request = reportingRequest.request();
            reportingPublishingService.reportProviderVerificationDetailsLog(request);
        }
        return verificationResult;
    }

    public VerificationResult verify(ContainerVerificationRequest containerVerificationRequest) {
        VerificationResult verificationResult = containerVerification.verifyRequest(containerVerificationRequest);
        if (verificationResult.isReportable()) {
            ContainerVerificationLogRequest request = new ContainerVerificationReportRequest(
                    containerVerificationRequest,
                    verificationResult.isSuccess()
            ).request();
            reportingPublishingService.reportContainerVerificationDetailsLog(request);
        }
        return verificationResult;
    }

    public VerificationResult verify(IvrContainerRegistrationRequest request) throws IOException, TemplateException {
        VerificationResult verificationResult = containerRegistrationVerification.verifyRequest(request);
        if (verificationResult.isSuccess()) {
            ContainerRegistrationRequestMapper containerRegistrationRequestMapper = new ContainerRegistrationRequestMapper(providerService);
            ContainerRegistrationRequest containerRegistrationReportingRequest = containerRegistrationRequestMapper.buildContainerRegistrationRequest(request);
            containerService.registerContainer(containerRegistrationReportingRequest);
        }
        return verificationResult;
    }

    private String providerId(String phoneNumber, boolean successful) {
        if (successful) {
            return providerService.findByMobileNumber(phoneNumber).getProviderId();
        } else {
            return "";
        }
    }
}
