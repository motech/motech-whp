package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

@Component
public class ReminderEventHandler {

    private final ProviderReminderService providerReminderService;
    private final HttpClientService httpClientService;
    private IvrConfiguration ivrConfiguration;
    private UUIDGenerator UUIDGenerator;

    @Autowired
    public ReminderEventHandler(ProviderReminderService providerReminderService, HttpClientService httpClientService, IvrConfiguration ivrConfiguration, UUIDGenerator uuidGenerator) {
        this.providerReminderService = providerReminderService;
        this.httpClientService = httpClientService;
        this.ivrConfiguration = ivrConfiguration;
        this.UUIDGenerator = uuidGenerator;
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME)
    public void adherenceWindowApproachingEvent(MotechEvent motechEvent) {
        List<String> providerPhoneNumbers = providerReminderService.getActiveProviderPhoneNumbers();
        if (providerPhoneNumbers.isEmpty()) {
            return;
        }
        ProviderReminderRequest providerReminderRequest = new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING, providerPhoneNumbers, UUIDGenerator.uuid());
        httpClientService.post(ivrConfiguration.getProviderReminderUrl(), providerReminderRequest.toXML());
    }
}
