package org.motechproject.whp.container.tracking.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.patient.builder.PatientBuilder;
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

public class ContainerTrackingServiceTest {

    ContainerTrackingService containerTrackingService;

    @Mock
    AllContainerTrackingRecords allContainerTrackingRecords;
    @Mock
    AllProviders allProviders;
    @Mock
    private AllPatients allPatients;
    @Mock
    private AllReasonForContainerClosures allReasonForContainerClosures;
    @Mock
    private AllAlternateDiagnosis allAlternateDiagnosis;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingService = new ContainerTrackingService(allContainerTrackingRecords, allProviders, allPatients, allReasonForContainerClosures, allAlternateDiagnosis);
    }

    @Test
    public void shouldCreateDashboardRowForContainer() {
        Container container = mock(Container.class);
        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);

        containerTrackingService.createDashboardRow(container);

        verify(allContainerTrackingRecords).add(row);
    }

    @Test
    public void shouldSaveProviderInformationInDashboardRowForContainerMappedToProvider() {
        Container container = new Container();
        container.setProviderId("providerId");
        Provider provider = new Provider();

        when(allProviders.findByProviderId("providerId")).thenReturn(provider);
        containerTrackingService.createDashboardRow(container);

        assertTrue(isProviderSaved(provider));
    }

    @Test
    public void shouldNotSaveProviderInformationInDashboardRowForContainerNotMappedToProvider() {
        Container container = new Container();
        container.setProviderId(null);

        containerTrackingService.createDashboardRow(container);

        assertTrue(isProviderNull());
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        List<ContainerTrackingRecord> containerTrackingRecords = Collections.emptyList();

        when(allContainerTrackingRecords.getAll()).thenReturn(containerTrackingRecords);

        assertEquals(containerTrackingRecords, containerTrackingService.allContainerDashboardRows());
    }

    @Test
    public void shouldSavePatientInformationInDashboardRowForContainerMappedToPatient() {
        Container container = existingContainer("patientId", "providerId");

        Patient patient = mock(Patient.class);
        when(allPatients.findByPatientId(container.getPatientId())).thenReturn(patient);

        containerTrackingService.updateDashboardRow(container);
        isPatientSaved(patient);
    }

    @Test
    public void shouldSaveNotPatientInformationInDashboardRowForContainerNotMappedToPatient() {
        Container container = existingContainer("patientId", "providerId");

        container.setPatientId(null);
        containerTrackingService.updateDashboardRow(container);
        isPatientRemoved();
    }

    @Test
    public void shouldRefreshProviderInformationInDashboardRowWhenContainerUpdated() {
        Container container = existingContainer("patientId", "providerId");

        Provider provider = mock(Provider.class);
        when(allProviders.findByProviderId(container.getProviderId())).thenReturn(provider);

        containerTrackingService.updateDashboardRow(container);
        isProviderRefreshed(provider);
    }

    @Test
    public void shouldRefreshContainerInformationInDashboardRowWhenContainerUpdated() {
        Container container = new Container();
        String patientId = "patientId";
        String providerId = "providerId";
        container.setPatientId(patientId);
        container.setProviderId(providerId);

        container.setContainerId(existingContainer(patientId, providerId).getContainerId());
        LabResults labResults = new LabResults();
        labResults.setLabName("myLabName");
        container.setLabResults(labResults);

        containerTrackingService.updateDashboardRow(container);

        isContainerRefreshed(container);
    }

    @Test
    public void shouldUpdateProviderInformationForAllRowsMappedToTheProvider() {
        ContainerTrackingRecord rowToBeUpdated1 = new ContainerTrackingRecord();
        ContainerTrackingRecord rowToBeUpdated2 = new ContainerTrackingRecord();

        when(allContainerTrackingRecords.withProviderId("providerid")).thenReturn(asList(rowToBeUpdated1, rowToBeUpdated2));

        Provider provider = new Provider();
        provider.setProviderId("providerId");
        containerTrackingService.updateProviderInformation(provider);

        verify(allContainerTrackingRecords).updateAll(asList(rowToBeUpdated1, rowToBeUpdated2));
    }

    @Test
    public void shouldUpdatePatientInformationForAllRowsMappedToThePatient() {
        ContainerTrackingRecord rowToBeUpdated1 = new ContainerTrackingRecord();
        ContainerTrackingRecord rowToBeUpdated2 = new ContainerTrackingRecord();

        when(allContainerTrackingRecords.withPatientId("patientid")).thenReturn(asList(rowToBeUpdated1, rowToBeUpdated2));

        Patient patient = new PatientBuilder().withDefaults().withPatientId("patientId").build();
        containerTrackingService.updatePatientInformation(patient);

        verify(allContainerTrackingRecords).updateAll(asList(rowToBeUpdated1, rowToBeUpdated2));
    }

    private void isContainerRefreshed(Container container) {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).update(captor.capture());
        ContainerTrackingRecord trackingRecord = captor.getValue();
        assertEquals(container.getLabResults().getLabName(), trackingRecord.getContainer().getLabResults().getLabName());
    }

    private Container existingContainer(String patientId, String providerId) {
        Container container = new Container();
        container.setPatientId(patientId);
        container.setProviderId(providerId);

        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(container);
        when(allContainerTrackingRecords.findByContainerId(container.getContainerId())).thenReturn(row);
        return container;
    }

    private boolean isPatientSaved(Patient expectedPatient) {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).update(captor.capture());
        return expectedPatient.equals(captor.getValue().getPatient());
    }

    private boolean isPatientRemoved() {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).update(captor.capture());
        return null == captor.getValue().getPatient();
    }

    private boolean isProviderSaved(Provider expectedProvider) {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).add(captor.capture());
        return expectedProvider.equals(captor.getValue().getProvider());
    }

    private boolean isProviderNull() {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).add(captor.capture());
        return null == captor.getValue().getProvider();
    }

    private boolean isProviderRefreshed(Provider expectedProvider) {
        ArgumentCaptor<ContainerTrackingRecord> captor = ArgumentCaptor.forClass(ContainerTrackingRecord.class);
        verify(allContainerTrackingRecords).update(captor.capture());
        return expectedProvider.equals(captor.getValue().getProvider());
    }
}
