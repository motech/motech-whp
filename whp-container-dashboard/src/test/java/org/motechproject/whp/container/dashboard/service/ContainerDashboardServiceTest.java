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

import static java.util.Arrays.asList;
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
    public void shouldSaveProviderInformationInDashboardRowForContainerMappedToProvider() {
        Container container = new Container();
        container.setProviderId("providerId");
        Provider provider = new Provider();

        when(allProviders.findByProviderId("providerId")).thenReturn(provider);
        containerDashboardService.createDashboardRow(container);

        assertTrue(isProviderSaved(provider));
    }

    @Test
    public void shouldNotSaveProviderInformationInDashboardRowForContainerNotMappedToProvider() {
        Container container = new Container();
        container.setProviderId(null);

        containerDashboardService.createDashboardRow(container);

        assertTrue(isProviderNull());
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<ContainerDashboardRow> containerDashboardRows = Collections.emptyList();

        when(allContainerDashboardRows.getAll()).thenReturn(containerDashboardRows);

        assertEquals(containerDashboardRows, containerDashboardService.allContainerDashboardRows());
    }

    @Test
    public void shouldSavePatientInformationInDashboardRowForContainerMappedToPatient() {
        Container container = existingContainer("patientId", "providerId");

        Patient patient = mock(Patient.class);
        when(allPatients.findByPatientId(container.getPatientId())).thenReturn(patient);

        containerDashboardService.updateDashboardRow(container);
        isPatientSaved(patient);
    }

    @Test
    public void shouldSaveNotPatientInformationInDashboardRowForContainerNotMappedToPatient() {
        Container container = existingContainer("patientId", "providerId");

        container.setPatientId(null);
        containerDashboardService.updateDashboardRow(container);
        isPatientRemoved();
    }

    @Test
    public void shouldRefreshProviderInformationInDashboardRowWhenContainerUpdated() {
        Container container = existingContainer("patientId", "providerId");

        Provider provider = mock(Provider.class);
        when(allProviders.findByProviderId(container.getProviderId())).thenReturn(provider);

        containerDashboardService.updateDashboardRow(container);
        isProviderRefreshed(provider);
    }

    @Test
    public void shouldUpdateProviderInformationForAllRowsMappedToTheProvider() {
        ContainerDashboardRow rowToBeUpdated1 = new ContainerDashboardRow();
        ContainerDashboardRow rowToBeUpdated2 = new ContainerDashboardRow();
        ContainerDashboardRow rowToBeIgnored = new ContainerDashboardRow();

        when(allContainerDashboardRows.getAll()).thenReturn(asList(rowToBeUpdated1, rowToBeUpdated2, rowToBeIgnored));
        when(allContainerDashboardRows.withProviderId("providerid")).thenReturn(asList(rowToBeUpdated1, rowToBeUpdated2));

        Provider provider = new Provider();
        provider.setProviderId("providerId");
        containerDashboardService.updateProviderInformation(provider);

        verify(allContainerDashboardRows).updateAll(asList(rowToBeUpdated1, rowToBeUpdated2));
    }

    private Container existingContainer(String patientId, String providerId) {
        Container container = new Container();
        container.setPatientId(patientId);
        container.setProviderId(providerId);

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);
        when(allContainerDashboardRows.findByContainerId(container.getContainerId())).thenReturn(row);
        return container;
    }

    private boolean isPatientSaved(Patient expectedPatient) {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).update(captor.capture());
        return expectedPatient.equals(captor.getValue().getPatient());
    }

    private boolean isPatientRemoved() {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).update(captor.capture());
        return null == captor.getValue().getPatient();
    }

    private boolean isProviderSaved(Provider expectedProvider) {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).add(captor.capture());
        return expectedProvider.equals(captor.getValue().getProvider());
    }

    private boolean isProviderNull() {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).add(captor.capture());
        return null == captor.getValue().getProvider();
    }

    private boolean isProviderRefreshed(Provider expectedProvider) {
        ArgumentCaptor<ContainerDashboardRow> captor = ArgumentCaptor.forClass(ContainerDashboardRow.class);
        verify(allContainerDashboardRows).update(captor.capture());
        return expectedProvider.equals(captor.getValue().getProvider());
    }
}
