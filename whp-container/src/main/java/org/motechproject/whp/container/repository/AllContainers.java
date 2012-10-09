package org.motechproject.whp.container.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllContainers extends MotechBaseRepository<Container> {

    private EventContext eventContext;

    @Autowired
    public AllContainers(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, EventContext eventContext) {
        super(Container.class, dbCouchDbConnector);
        this.eventContext = eventContext;
    }

    @GenerateView
    public Container findByContainerId(String containerId) {
        if (containerId == null)
            return null;
        ViewQuery find_by_containerId = createQuery("by_containerId").key(containerId.toLowerCase()).includeDocs(true);
        return singleResult(db.queryView(find_by_containerId, Container.class));
    }

    @Override
    public void add(Container entity) {
        super.add(entity);
        eventContext.send(WHPContainerConstants.CONTAINER_ADDED_SUBJECT, entity);
    }

    @Override
    public void update(Container entity) {
        super.update(entity);
        eventContext.send(WHPContainerConstants.CONTAINER_UPDATED_SUBJECT, entity);
    }
}