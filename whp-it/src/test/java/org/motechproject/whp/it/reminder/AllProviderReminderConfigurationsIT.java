package org.motechproject.whp.it.reminder;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.repository.AllProviderReminderConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_NOT_REPORTED;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllProviderReminderConfigurationsIT extends SpringIntegrationTest {

    @Autowired
    AllProviderReminderConfigurations allProviderReminderConfigurations;

    @Before
    @After
    public void tearDown() {
        allProviderReminderConfigurations.removeAll();
    }

    @Test
    public void shouldSaveProviderReminderConfiguration() {
        ProviderReminderConfiguration providerReminderConfiguration = configuration(DayOfWeek.Monday, 1, 1);
        allProviderReminderConfigurations.add(providerReminderConfiguration);
        assertNotNull(allProviderReminderConfigurations.get(providerReminderConfiguration.getId()));
    }

    @Test
    public void shouldNotMaintainDuplicateCopiesOfConfiguration() {
        ProviderReminderConfiguration[] configurations = {configuration(DayOfWeek.Monday, 1, 1), configuration(DayOfWeek.Tuesday, 1, 1)};

        allProviderReminderConfigurations.saveOrUpdate(configurations[0]);
        allProviderReminderConfigurations.saveOrUpdate(configurations[1]);

        assertTrue(StringUtils.isNotBlank(configurations[0].getId()));
        assertEquals(configurations[0].getId(), configurations[1].getId());
        assertEquals(DayOfWeek.Tuesday, configurations[1].getDayOfWeek());
    }

    private ProviderReminderConfiguration configuration(DayOfWeek dayOfWeek, int hour, int minute) {
        ProviderReminderConfiguration providerReminderConfiguration = new ProviderReminderConfiguration();
        providerReminderConfiguration.setDayOfWeek(dayOfWeek);
        providerReminderConfiguration.setHour(hour);
        providerReminderConfiguration.setMinute(minute);
        providerReminderConfiguration.setReminderType(ADHERENCE_NOT_REPORTED);
        return providerReminderConfiguration;
    }
}