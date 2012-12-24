package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderEventHandler {

    private final ProviderReminderService providerReminderService;

    @Autowired
    public ReminderEventHandler(ProviderReminderService providerReminderService) {
        this.providerReminderService = providerReminderService;
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME)
    public void adherenceWindowApproachingEvent(MotechEvent motechEvent) {
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING);
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME)
    public void adherenceNotReportedEvent(MotechEvent motechEvent) {
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED);
    }
}
