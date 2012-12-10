package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

@Component
public class ReminderEventHandler {

    private final ProviderReminderService providerReminderService;
    private final HttpClientService httpClientService;
    private IvrConfiguration ivrConfiguration;

    @Autowired
    public ReminderEventHandler(ProviderReminderService providerReminderService, HttpClientService httpClientService, IvrConfiguration ivrConfiguration) {
        this.providerReminderService = providerReminderService;
        this.httpClientService = httpClientService;
        this.ivrConfiguration = ivrConfiguration;
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_WINDOW_APPROACHING_SUBJECT)
    public void adherenceWindowApproachingEvent(MotechEvent motechEvent) {
        List<String> providerPhoneNumbers = providerReminderService.getActiveProviderPhoneNumbers();
        httpClientService.post(ivrConfiguration.getProviderReminderUrl(), new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING.name(), providerPhoneNumbers));
    }
}
