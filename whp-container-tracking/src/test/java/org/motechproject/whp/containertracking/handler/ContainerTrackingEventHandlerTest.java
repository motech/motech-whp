package org.motechproject.whp.containertracking.handler;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.containertracking.builder.ContainerTrackingEventsBuilder;
import org.motechproject.whp.containertracking.service.ContainerTrackingService;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerTrackingEventHandlerTest {

    @Mock
    private ContainerTrackingService containerTrackingService;

    private ContainerTrackingEventHandler containerTrackingEventHandler;

    @Before
    public void setup() {
        initMocks(this);
        containerTrackingEventHandler = new ContainerTrackingEventHandler(containerTrackingService);
    }

    @Test
    public void shouldCreateNewDashboardPageWhenContainerAdded() {
        MotechEvent event = new ContainerTrackingEventsBuilder().containerAddedEvent();

        containerTrackingEventHandler.onContainerAdded(event);

        Container container = (Container) event.getParameters().get("0");
        verify(containerTrackingService).createDashboardRow(container);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenContainerGotUpdated() {
        MotechEvent event = new ContainerTrackingEventsBuilder().containerAddedEvent();

        containerTrackingEventHandler.onContainerUpdated(event);

        Container container = (Container) event.getParameters().get("0");
        verify(containerTrackingService).updateDashboardRow(container);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenProviderGotUpdated() {
        MotechEvent event = new ContainerTrackingEventsBuilder().providerUpdatedEvent();

        containerTrackingEventHandler.onProviderUpdated(event);

        Provider provider = (Provider) event.getParameters().get("0");
        verify(containerTrackingService).updateProviderInformation(provider);
    }

    @Test
    public void shouldUpdateExistingDashboardPageWhenPatientGotUpdated() {
        MotechEvent event = new ContainerTrackingEventsBuilder().patientUpdatedEvent();

        containerTrackingEventHandler.onPatientUpdated(event);

        Patient patient = (Patient) event.getParameters().get("0");
        verify(containerTrackingService).updatePatientInformation(patient);
    }
}
