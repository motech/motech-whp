package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ReminderEventHandlerTest {

    ReminderEventHandler reminderEventHandler;

    @Mock
    HttpClientService httpClientService;
    @Mock
    private ProviderReminderService providerReminderService;
    @Mock
    private IvrConfiguration ivrConfiguration;
    @Mock
    private UUIDGenerator UUIDGenerator;


    @Before
    public void setUp() {
        initMocks(this);
        reminderEventHandler = new ReminderEventHandler(providerReminderService, httpClientService, ivrConfiguration, UUIDGenerator);
    }

    @Test
    public void shouldHandleEventForAdherenceWindowApproachingAlert() {
        String url = "some wgn url";
        String requestId = "requestId";
        List<String> msisdnList = asList("msisdn1", "msisdn2");
        when(providerReminderService.getActiveProviderPhoneNumbers()).thenReturn(msisdnList);
        when(ivrConfiguration.getProviderReminderUrl()).thenReturn(url);
        when(UUIDGenerator.uuid()).thenReturn(requestId);

        reminderEventHandler.adherenceWindowApproachingEvent(new MotechEvent(EventKeys.ADHERENCE_WINDOW_APPROACHING_SUBJECT));

        verify(providerReminderService).getActiveProviderPhoneNumbers();
        verify(httpClientService).post(url, new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING.name(), msisdnList, requestId).toXML());
    }
}
