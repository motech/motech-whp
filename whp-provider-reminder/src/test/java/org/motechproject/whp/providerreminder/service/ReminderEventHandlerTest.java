package org.motechproject.whp.providerreminder.service;

import org.apache.commons.collections.ListUtils;
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
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_NOT_REPORTED;
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
    private UUIDGenerator uuidGenerator;


    @Before
    public void setUp() {
        initMocks(this);
        reminderEventHandler = new ReminderEventHandler(providerReminderService, httpClientService, ivrConfiguration, uuidGenerator);
    }

    @Test
    public void shouldHandleEventForAdherenceWindowApproachingAlert() {
        String url = "some wgn url";
        String requestId = "requestId";
        List<String> msisdnList = asList("msisdn1", "msisdn2");
        when(providerReminderService.getActiveProviderPhoneNumbers()).thenReturn(msisdnList);
        when(ivrConfiguration.getProviderReminderUrl()).thenReturn(url);
        when(uuidGenerator.uuid()).thenReturn(requestId);

        reminderEventHandler.adherenceWindowApproachingEvent(new MotechEvent(EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME));

        verify(providerReminderService).getActiveProviderPhoneNumbers();
        verify(httpClientService).post(url, new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING, msisdnList, requestId).toXML());
    }

    @Test
    public void shouldNotSendRequestToIvrSystemIfThereAreNoActiveProviders() {
        when(providerReminderService.getActiveProviderPhoneNumbers()).thenReturn(ListUtils.EMPTY_LIST);

        reminderEventHandler.adherenceWindowApproachingEvent(new MotechEvent(EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME));

        verify(providerReminderService).getActiveProviderPhoneNumbers();
        verifyNoMoreInteractions(httpClientService);
    }

    @Test
    public void shouldHandleEventForAdherenceNotReportedAlert() {
        String url = "some wgn url";
        String requestId = "requestId";
        List<String> msisdnList = asList("msisdn1", "msisdn2");
        when(providerReminderService.getProviderPhoneNumbersWithPendingAdherence()).thenReturn(msisdnList);
        when(ivrConfiguration.getProviderReminderUrl()).thenReturn(url);
        when(uuidGenerator.uuid()).thenReturn(requestId);

        reminderEventHandler.adherenceNotReportedEvent(new MotechEvent(EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME));

        verify(providerReminderService).getProviderPhoneNumbersWithPendingAdherence();
        verify(httpClientService).post(url, new ProviderReminderRequest(ADHERENCE_NOT_REPORTED, msisdnList, requestId).toXML());
    }
}
