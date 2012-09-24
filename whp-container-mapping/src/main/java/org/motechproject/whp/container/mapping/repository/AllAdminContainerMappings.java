package org.motechproject.whp.container.mapping.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.mapping.domain.AdminContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllAdminContainerMappings extends MotechBaseRepository<AdminContainerMapping> {

    @Autowired
    public AllAdminContainerMappings(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(AdminContainerMapping.class, db);
    }
}
