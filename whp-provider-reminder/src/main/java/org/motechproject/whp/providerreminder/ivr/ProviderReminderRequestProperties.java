package org.motechproject.whp.providerreminder.ivr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProviderReminderRequestProperties {

    @Value("#{providerReminderProperty['provider.reminder.batch.size']}")
    private int batchSize;

    public int getBatchSize() {
        return batchSize;
    }
}
