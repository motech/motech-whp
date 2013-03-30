package org.motechproject.whp.containermapping.service;

import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.couchdbcrud.service.CrudEntity;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class ProviderContainerMappingCrudEntity extends CrudEntity<ProviderContainerMapping> {

    AllProviderContainerMappings allProviderContainerMappings;

    @Autowired
    public ProviderContainerMappingCrudEntity(AllProviderContainerMappings allProviderContainerMappings) {
        this.allProviderContainerMappings = allProviderContainerMappings;
    }

    @Override
    public List<String> getDisplayFields() {
        return asList("providerId");
    }

    @Override
    public List<String> getFilterFields() {
        return asList("providerId");
    }

    @Override
    public CrudRepository getRepository() {
        return allProviderContainerMappings;
    }

    @Override
    public Class getEntityType() {
        return ProviderContainerMapping.class;
    }
}
