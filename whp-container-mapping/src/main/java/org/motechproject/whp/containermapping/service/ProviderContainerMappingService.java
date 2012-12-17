package org.motechproject.whp.containermapping.service;

import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Long.parseLong;

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
        return providerContainerMapping != null && providerContainerMapping.hasContainerId(parseLong(containerId));
    }
}
