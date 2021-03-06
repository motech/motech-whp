package org.motechproject.whp.containerregistration.mapper;


import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.ProviderService;

public class ContainerRegistrationRequestMapper {

    private ProviderService providerService;

    public ContainerRegistrationRequestMapper(ProviderService providerService) {
        this.providerService = providerService;
    }

    public ContainerRegistrationRequest buildContainerRegistrationRequest(IvrContainerRegistrationRequest ivrContainerRegistrationRequest) {
        Provider provider = providerService.findByMobileNumber(ivrContainerRegistrationRequest.getMsisdn());
        ContainerRegistrationRequest containerRegistrationReportingRequest = new ContainerRegistrationRequest(provider.getProviderId(),
                ivrContainerRegistrationRequest.getContainer_id(),
                ivrContainerRegistrationRequest.getPhase(),
                ChannelId.IVR.name(),
                ivrContainerRegistrationRequest.getCall_id()
        );
        containerRegistrationReportingRequest.setSubmitterId(provider.getProviderId());
        containerRegistrationReportingRequest.setSubmitterRole(WHPRole.PROVIDER.name());
        return containerRegistrationReportingRequest;
    }
}