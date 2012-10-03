package org.motechproject.whp.container.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllContainersTest {

    @Mock
    private CouchDbConnector couchDbConnector;

    @Mock
    private EventRelay eventRelay;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldRaiseEventToIndicateContainerGotAdded() {
        AllContainers allContainers = new AllContainers(couchDbConnector, eventRelay);
        Container container = mock(Container.class);
        allContainers.add(container);

        assertTrue(eventRaisedWithCorrectSubject());
        assertTrue(eventRaisedWithContainerAsParameter(container));
    }

    private boolean eventRaisedWithCorrectSubject() {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        MotechEvent raisedEvent = captor.getValue();

        return WHPContainerConstants.CONTAINER_ADDED_SUBJECT.equals(raisedEvent.getSubject());
    }

    private boolean eventRaisedWithContainerAsParameter(Container container) {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        MotechEvent raisedEvent = captor.getValue();

        return container.equals(raisedEvent.getParameters().get(WHPContainerConstants.CONTAINER_ADDED_CONTAINER));
    }
}
