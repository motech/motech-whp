package org.motechproject.whp.crud;

import org.ektorp.CouchDbConnector;
import org.motechproject.crud.repository.CouchDBCrudRepository;
import org.motechproject.crud.service.CouchDBCrudEntity;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class ProviderContainerMappingCrudEntity extends CouchDBCrudEntity<ProviderContainerMapping> {

    @Autowired
    public ProviderContainerMappingCrudEntity(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(new CouchDBCrudRepository(ProviderContainerMapping.class, db));
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
