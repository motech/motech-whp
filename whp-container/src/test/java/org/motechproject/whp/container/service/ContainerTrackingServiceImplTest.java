package org.motechproject.whp.container.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerTrackingServiceImplTest {

    ContainerTrackingService containerTrackingService;

    @Mock
    AllContainerTrackingRecords allContainerTrackingRecords;
    @Mock
    AllProviders allProviders;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingService = new ContainerTrackingService(allContainerTrackingRecords, allProviders);
    }


    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<Container> containerTrackingRecords = Collections.emptyList();

        when(allContainerTrackingRecords.getAll()).thenReturn(containerTrackingRecords);

        assertEquals(containerTrackingRecords, containerTrackingService.allContainerDashboardRows());
    }

    @Test
    public void shouldUpdateProviderInformationForAllRowsMappedToTheProvider() {
        Container rowToBeUpdated1 = new Container();
        Container rowToBeUpdated2 = new Container();

        when(allContainerTrackingRecords.withProviderId("providerid")).thenReturn(asList(rowToBeUpdated1, rowToBeUpdated2));

        Provider provider = new Provider();
        provider.setProviderId("providerId");
        containerTrackingService.updateProviderInformation(provider);

        verify(allContainerTrackingRecords).updateAll(asList(rowToBeUpdated1, rowToBeUpdated2));
    }

}
