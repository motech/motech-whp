package org.motechproject.whp.container.mapping.service;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.matcher.LambdaJMatcher;
import org.motechproject.whp.container.mapping.domain.ContainerRange;
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
        if(providerContainerMapping == null) return false;
        return Lambda.exists(providerContainerMapping.getContainerRanges(), whichIncludes(Long.parseLong(containerId)));
    }

    private LambdaJMatcher<Boolean> whichIncludes(final long containerId) {
        return new LambdaJMatcher<Boolean>() {
            @Override
            public boolean matches(Object o) {
                ContainerRange range = (ContainerRange) o;
                return range.includes(containerId);
            }
        };
    }
}
