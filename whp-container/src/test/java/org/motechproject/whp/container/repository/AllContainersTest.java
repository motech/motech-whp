package org.motechproject.whp.container.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllContainersTest {

    @Mock
    private CouchDbConnector couchDbConnector;

    @Mock
    private EventContext eventContext;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldRaiseEventToIndicateContainerGotAdded() {
        AllContainers allContainers = new AllContainers(couchDbConnector, eventContext);
        Container container = mock(Container.class);

        allContainers.add(container);

        verify(eventContext).send(WHPContainerConstants.CONTAINER_ADDED_SUBJECT, container);
    }

    @Test
    public void shouldRaiseEventToIndicateContainerGotUpdated() {
        AllContainers allContainers = new AllContainers(couchDbConnector, eventContext);
        Container container = mock(Container.class);

        allContainers.update(container);

        verify(eventContext).send(WHPContainerConstants.CONTAINER_UPDATED_SUBJECT, container);
    }
}
