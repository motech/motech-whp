package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME;
import static org.motechproject.whp.common.event.EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME;

public class ReminderEventHandlerTest {

    public static final String UUID = "uuid";
    public static final String IVRUrl = "some wgn url";

    @Mock
    private ProviderReminderService providerReminderService;
    @Mock
    private IvrConfiguration ivrConfiguration;
    @Mock
    private UUIDGenerator uuidGenerator;

    ReminderEventHandler reminderEventHandler;

    @Before
    public void setUp() {
        initMocks(this);
        setupIVRConfiguration();
        setupUUID();
        reminderEventHandler = new ReminderEventHandler(providerReminderService, ivrConfiguration, uuidGenerator);
    }

    private void setupUUID() {
        when(uuidGenerator.uuid()).thenReturn(UUID);
    }

    private void setupIVRConfiguration() {
        when(ivrConfiguration.getProviderReminderUrl()).thenReturn(IVRUrl);
    }

    @Test
    public void shouldRemindProvidersWhenAdherenceWindowApproaches() {
        MotechEvent motechEvent = new MotechEvent(ADHERENCE_WINDOW_APPROACHING_EVENT_NAME);
        reminderEventHandler.adherenceWindowApproachingEvent(motechEvent);
        verify(providerReminderService).alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING, IVRUrl, UUID);
    }

    @Test
    public void shouldRemindProvidersPendingAdherence() {
        MotechEvent motechEvent = new MotechEvent(ADHERENCE_NOT_REPORTED_EVENT_NAME);
        reminderEventHandler.adherenceNotReportedEvent(motechEvent);
        verify(providerReminderService).alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED, IVRUrl, UUID);
    }

    @Test
    public void shouldRemindProvidersWithNewUUIDOnEveryRequest() {
        MotechEvent motechEvent = new MotechEvent(ADHERENCE_NOT_REPORTED_EVENT_NAME);
        reminderEventHandler.adherenceNotReportedEvent(motechEvent);
        reminderEventHandler.adherenceNotReportedEvent(motechEvent);
        verify(uuidGenerator, times(2)).uuid();
    }
}
