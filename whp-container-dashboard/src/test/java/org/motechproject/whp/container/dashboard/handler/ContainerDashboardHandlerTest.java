package org.motechproject.whp.container.dashboard.handler;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.dashboard.builder.DashboardEventsBuilder;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.container.WHPContainerConstants.CONTAINER_KEY;
import static org.motechproject.whp.patient.WHPPatientConstants.PATIENT_KEY;
import static org.motechproject.whp.user.WHPUserConstants.PROVIDER_KEY;

public class ContainerDashboardHandlerTest {

    @Mock
    private ContainerDashboardService containerDashboardService;

    private ContainerDashboardHandler containerDashboardHandler;

    @Before
    public void setup() {
        initMocks(this);
        containerDashboardHandler = new ContainerDashboardHandler(containerDashboardService);
    }

    @Test
    public void shouldCreateNewDashboardPageWhenContainerAdded() {
        MotechEvent event = new DashboardEventsBuilder().containerAddedEvent();

        containerDashboardHandler.onContainerAdded(event);

        Container container = (Container) event.getParameters().get(CONTAINER_KEY);
        verify(containerDashboardService).createDashboardRow(container);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenContainerGotUpdated() {
        MotechEvent event = new DashboardEventsBuilder().containerAddedEvent();

        containerDashboardHandler.onContainerUpdated(event);

        Container container = (Container) event.getParameters().get(CONTAINER_KEY);
        verify(containerDashboardService).updateDashboardRow(container);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenProviderGotUpdated() {
        MotechEvent event = new DashboardEventsBuilder().providerUpdatedEvent();

        containerDashboardHandler.onProviderUpdated(event);

        Provider provider = (Provider) event.getParameters().get(PROVIDER_KEY);
        verify(containerDashboardService).updateProviderInformation(provider);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenPatientGotUpdated() {
        MotechEvent event = new DashboardEventsBuilder().patientUpdatedEvent();

        containerDashboardHandler.onPatientUpdated(event);

        Patient patient = (Patient) event.getParameters().get(PATIENT_KEY);
        verify(containerDashboardService).updatePatientInformation(patient);
    }
}
