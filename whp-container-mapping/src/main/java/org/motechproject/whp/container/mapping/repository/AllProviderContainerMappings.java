package org.motechproject.whp.container.mapping.repository;


import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.mapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllProviderContainerMappings  extends MotechBaseRepository<ProviderContainerMapping> {

    @Autowired
    public AllProviderContainerMappings(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(ProviderContainerMapping.class, db);
    }
}
