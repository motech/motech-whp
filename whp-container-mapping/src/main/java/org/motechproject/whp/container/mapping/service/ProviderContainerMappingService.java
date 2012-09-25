package org.motechproject.whp.container.mapping.service;

import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.motechproject.whp.container.mapping.repository.AllProviderContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderContainerMappingService {

    private AllProviderContainerMappings allProviderContainerMappings;

    @Autowired
    public ProviderContainerMappingService(AllProviderContainerMappings allProviderContainerMappings) {
        this.allProviderContainerMappings = allProviderContainerMappings;
    }

    public void add(ProviderContainerMapping providerContainerMapping) {
        allProviderContainerMappings.add(providerContainerMapping);
    }

    public Boolean isValidContainerForProvider(String providerId, String containerId) {
        ProviderContainerMapping providerContainerMapping = allProviderContainerMappings.findByProviderId(providerId);
        if(providerContainerMapping == null)
            return false;
        return providerContainerMapping.hasContainerId(Long.parseLong(containerId));
    }
}
