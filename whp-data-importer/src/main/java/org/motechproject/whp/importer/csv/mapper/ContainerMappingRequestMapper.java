package org.motechproject.whp.importer.csv.mapper;

import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.importer.csv.request.ContainerMappingRequest;
import org.springframework.stereotype.Component;

import static java.lang.Long.parseLong;

@Component
public class ContainerMappingRequestMapper {


    public ProviderContainerMapping map(ContainerMappingRequest request) {

        ProviderContainerMapping providerContainerMapping = new ProviderContainerMapping();
        providerContainerMapping.setProviderId(request.getProviderId());
        providerContainerMapping.add(new ContainerRange(parseLong(request.getContainerIdFrom()), parseLong(request.getContainerIdTo())));

        return providerContainerMapping;
    }
}
