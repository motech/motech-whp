package org.motechproject.whp.it.common.service;

import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ProviderReminderConfigurationIT extends SpringIntegrationTest {

    @Autowired
    ProviderReminderConfiguration providerReminderConfiguration;

    @Test
    public void testGetProviderReminderHour() {
        assertNotNull(providerReminderConfiguration.getHour());
    }
}
