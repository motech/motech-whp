package org.motechproject.whp.mapper;


import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;

public class ContainerRegistrationRequestMapper {

    private ProviderService providerService;

    public ContainerRegistrationRequestMapper(ProviderService providerService) {

        this.providerService = providerService;
    }

    public ContainerRegistrationRequest buildContainerRegistrationRequest(IvrContainerRegistrationRequest ivrContainerRegistrationRequest){

        Provider provider = providerService.findByMobileNumber(ivrContainerRegistrationRequest.getMsisdn());
        ContainerRegistrationRequest containerRegistrationRequest = new ContainerRegistrationRequest(provider.getProviderId(),ivrContainerRegistrationRequest.getContainer_id(), ivrContainerRegistrationRequest.getPhase());
        return containerRegistrationRequest;
    }
}