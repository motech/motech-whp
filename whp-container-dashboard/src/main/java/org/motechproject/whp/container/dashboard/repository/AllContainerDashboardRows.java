package org.motechproject.whp.container.dashboard.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;


@Repository
public class AllContainerDashboardRows extends MotechBaseRepository<ContainerDashboardRow>{

    @Autowired
    public AllContainerDashboardRows(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(ContainerDashboardRow.class, dbCouchDbConnector);
    }

    @Override
    public List<ContainerDashboardRow> getAll() {
        return Collections.emptyList();
    }
}
