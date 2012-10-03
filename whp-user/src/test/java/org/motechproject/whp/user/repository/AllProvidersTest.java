package org.motechproject.whp.user.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllProvidersTest {

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CouchDbConnector dbConnector;

    private AllProviders allProviders;

    @Before
    public void setup() {
        initMocks(this);
        allProviders = new AllProviders(dbConnector, eventRelay);
    }

    @Test
    public void shouldRaiseEventWhenProviderIsUpdated() {
        Provider provider = new Provider();
        allProviders.update(provider);

        assertTrue(eventRaisedWithSubject(WHPUserConstants.PROVIDER_ADDED_SUBJECT));
        assertTrue(eventRaisedWithParameter(WHPUserConstants.PROVIDER_KEY, provider));
    }

    private boolean eventRaisedWithSubject(String providerAddedSubject) {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        return providerAddedSubject.equals(captor.getValue().getSubject());
    }

    private boolean eventRaisedWithParameter(String key, Object value) {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        return value.equals(captor.getValue().getParameters().get(key));
    }
}
