package org.motechproject.whp.container.dashboard.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
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
    @Mock
    private AllPatients allPatients;

    ContainerDashboardService containerDashboardService;

    @Before
    public void setUp() {
        initMocks(this);
        containerDashboardService = new ContainerDashboardService(allContainerDashboardRows, allProviders, allPatients);
    }

    @Test
    public void shouldCreateDashboardRowForContainer() {
        Container container = mock(Container.class);
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);

        containerDashboardService.createDashboardRow(container);

        verify(allContainerDashboardRows).add(row);
    }

    @Test
    public void shouldUpdateDashboardRowForContainer() {
        Container container = new Container();
        container.setPatientId("patientId");

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);

        Patient patient = mock(Patient.class);

        when(allPatients.findByPatientId(container.getPatientId())).thenReturn(patient);
        when(allContainerDashboardRows.findByContainerId(container.getContainerId())).thenReturn(row);

        containerDashboardService.updateDashboardRow(container);

        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).update(captor.capture());
        ContainerDashboardRow updatedDashboardRow = captor.getValue();

        assertEquals(patient, updatedDashboardRow.getPatient());
    }

    @Test
    public void shouldSaveProviderInformationInDashboardRowForContainerMappedToProvider() {
        Container container = new Container();
        container.setProviderId("providerId");
        Provider provider = new Provider();

        when(allProviders.findByProviderId("providerId")).thenReturn(provider);
        containerDashboardService.createDashboardRow(container);

        assertTrue(isProviderPresent(provider));
    }

    @Test
    public void shouldNotSaveProviderInformationInDashboardRowForContainerNotMappedToProvider() {
        Container container = new Container();
        container.setProviderId(null);

        containerDashboardService.createDashboardRow(container);

        assertTrue(isNoProviderPresent());
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

    private boolean isNoProviderPresent() {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).add(captor.capture());
        return null == captor.getValue().getProvider();
    }
}
