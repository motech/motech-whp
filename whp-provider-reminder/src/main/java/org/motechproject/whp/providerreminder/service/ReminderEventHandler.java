package org.motechproject.whp.providerreminder.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderEventHandler {

    private final ProviderReminderService providerReminderService;
    private final UUIDGenerator uuidGenerator;
    private final String ivrUrl;

    @Autowired
    public ReminderEventHandler(ProviderReminderService providerReminderService, IvrConfiguration ivrConfiguration, UUIDGenerator uuidGenerator) {
        this.providerReminderService = providerReminderService;
        this.ivrUrl = ivrConfiguration.getProviderReminderUrl();
        this.uuidGenerator = uuidGenerator;
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME)
    public void adherenceWindowApproachingEvent(MotechEvent motechEvent) {
        String uuid = uuidGenerator.uuid();
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING, ivrUrl, uuid);
    }

    @MotechListener(subjects = EventKeys.ADHERENCE_NOT_REPORTED_EVENT_NAME)
    public void adherenceNotReportedEvent(MotechEvent motechEvent) {
        String uuid = uuidGenerator.uuid();
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED, ivrUrl, uuid);
    }
}
