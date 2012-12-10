package org.motechproject.whp.it.common.service;

import org.junit.Test;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class IvrConfigurationIT extends SpringIntegrationTest {

    @Autowired
    IvrConfiguration ivrConfiguration;

    @Test
    public void testGetProviderReminderUrl() {
        assertNotNull(ivrConfiguration.getProviderReminderUrl());
    }
}
