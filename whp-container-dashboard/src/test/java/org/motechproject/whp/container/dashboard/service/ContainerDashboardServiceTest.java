package org.motechproject.whp.container.dashboard.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerDashboardServiceTest {

    @Mock
    AllContainerDashboardRows allContainerDashboardRows;
    @Mock
    AllProviders allProviders;

    ContainerDashboardService containerDashboardService;

    @Before
    public void setUp() {
        initMocks(this);
        containerDashboardService = new ContainerDashboardService(allContainerDashboardRows, allProviders);
    }

    @Test
    public void shouldPersistContainerDashboardRow() {
        Container container = mock(Container.class);
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);

        containerDashboardService.createDashboardRow(container);

        verify(allContainerDashboardRows).add(row);
    }

    @Test
    public void shouldPersistProviderInformationWhenCreatingContainerDashboardRowWhenProviderIdIsMapped() {
        Container container = new Container();
        container.setProviderId("providerId");
        Provider provider = new Provider();

        when(allProviders.findByProviderId("providerId")).thenReturn(provider);
        containerDashboardService.createDashboardRow(container);

        assertTrue(isProviderPresent(provider));
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<ContainerDashboardRow> containerDashboardRows = Collections.emptyList();

        when(allContainerDashboardRows.getAll()).thenReturn(containerDashboardRows);

        assertEquals(containerDashboardRows, containerDashboardService.allContainerDashboardRows());
    }

    private boolean isProviderPresent(Provider expectedProvider) {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).add(captor.capture());
        return expectedProvider.equals(captor.getValue().getProvider());
    }
}
