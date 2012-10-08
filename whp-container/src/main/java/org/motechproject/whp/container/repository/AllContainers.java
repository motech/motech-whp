package org.motechproject.whp.container.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllContainers extends MotechBaseRepository<Container> {

    private EventRelay eventRelay;

    @Autowired
    public AllContainers(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, EventRelay eventRelay) {
        super(Container.class, dbCouchDbConnector);
        this.eventRelay = eventRelay;
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
        eventRelay.sendEventMessage(buildContainerEvent(WHPContainerConstants.CONTAINER_ADDED_SUBJECT, entity));
    }

    @Override
    public void update(Container entity) {
        super.update(entity);
        eventRelay.sendEventMessage(buildContainerEvent(WHPContainerConstants.CONTAINER_UPDATED_SUBJECT, entity));
    }

    private MotechEvent buildContainerEvent(String subject, Container container) {
        MotechEvent motechEvent = new MotechEvent(subject);
        motechEvent.getParameters().put(WHPContainerConstants.CONTAINER_KEY, container);
        return motechEvent;
    }
}