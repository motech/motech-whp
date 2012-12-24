package org.motechproject.whp.it.provider.reminder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.providerreminder.ivr.ProviderReminderRequestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationProviderReminderContext.xml")
public class ProviderReminderPropertiesIT {

    @Autowired
    private ProviderReminderRequestProperties providerReminderRequestProperties;

    @Test
    public void shouldReturnBatchConfigurationForProviderReminderScheduler() {
        assertEquals(30, providerReminderRequestProperties.getBatchSize());
    }
}
