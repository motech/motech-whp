package org.motechproject.whp.container.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllContainers extends MotechBaseRepository<Container> {
    @Autowired
    public AllContainers(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Container.class, dbCouchDbConnector);
    }

    @GenerateView
    public Container findByContainerId(String containerId) {
        if (containerId == null)
            return null;
        ViewQuery find_by_containerId = createQuery("by_containerId").key(containerId.toLowerCase()).includeDocs(true);
        return singleResult(db.queryView(find_by_containerId, Container.class));
    }

    @GenerateView
    public List<Container> findByPatientId(String patientId) {
        if (patientId == null)
            return null;
        ViewQuery find_by_patient_id = createQuery("by_patientId").key(patientId).includeDocs(true);
        return db.queryView(find_by_patient_id, Container.class);
    }
}
