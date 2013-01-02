package org.motechproject.whp.providerreminder.ivr;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ProviderReminderRequestProperties {
    @Value("#{providerReminderProperty['provider.reminder.batch.size']}")
    private int batchSize;

    @Value("#{providerReminderProperty['provider.reminder.url']}")
    private String providerReminderUrl;
}
