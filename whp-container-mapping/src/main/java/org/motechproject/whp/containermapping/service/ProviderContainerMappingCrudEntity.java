package org.motechproject.whp.containermapping.service;

import org.motechproject.couchdbcrud.service.CouchDBCrudEntity;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class ProviderContainerMappingCrudEntity extends CouchDBCrudEntity<ProviderContainerMapping> {

    @Autowired
    public ProviderContainerMappingCrudEntity(AllProviderContainerMappings allProviderContainerMappings) {
        super(allProviderContainerMappings);
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
    public Class getEntityType() {
        return ProviderContainerMapping.class;
    }
}
