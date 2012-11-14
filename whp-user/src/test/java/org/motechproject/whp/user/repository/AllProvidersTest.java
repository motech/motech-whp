package org.motechproject.whp.user.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.event.EventContext;
import org.motechproject.whp.user.domain.Provider;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.WHPUserConstants.PROVIDER_UPDATED_SUBJECT;

public class AllProvidersTest {

    @Mock
    private EventContext eventContext;

    @Mock
    private CouchDbConnector dbConnector;

    private AllProviders allProviders;

    @Before
    public void setup() {
        initMocks(this);
        allProviders = new AllProviders(dbConnector, eventContext);
    }

    @Test
    public void shouldRaiseEventWhenProviderIsUpdated() {
        Provider provider = new Provider();
        allProviders.update(provider);

        verify(eventContext).send(PROVIDER_UPDATED_SUBJECT, provider);
    }

}
