package org.motechproject.whp.it.reminder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.repository.AllProviderReminderConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        ProviderReminderConfiguration providerReminderConfiguration = configuration(DayOfWeek.Monday, 1, 1, ADHERENCE_NOT_REPORTED, false);
        allProviderReminderConfigurations.saveOrUpdate(providerReminderConfiguration);
        assertNotNull(allProviderReminderConfigurations.get(providerReminderConfiguration.getId()));
    }

    @Test
    public void shouldNotMaintainDuplicateCopiesOfConfiguration() {
        ProviderReminderType type = ADHERENCE_NOT_REPORTED;
        ProviderReminderConfiguration[] configurations = {configuration(DayOfWeek.Monday, 1, 1, type, false), configuration(DayOfWeek.Tuesday, 2, 2, type, true)};

        allProviderReminderConfigurations.saveOrUpdate(configurations[0]);
        allProviderReminderConfigurations.saveOrUpdate(configurations[1]);

        ProviderReminderConfiguration updatedConfiguration = allProviderReminderConfigurations.withType(type);
        assertEquals(configurations[1].getDayOfWeek(), updatedConfiguration.getDayOfWeek());
        assertEquals(configurations[1].getHour(), updatedConfiguration.getHour());
        assertEquals(configurations[1].getMinute(), updatedConfiguration.getMinute());
        assertEquals(configurations[1].isScheduled(), updatedConfiguration.isScheduled());
    }

    private ProviderReminderConfiguration configuration(DayOfWeek dayOfWeek, int hour, int minute, ProviderReminderType type, boolean scheduled) {
        ProviderReminderConfiguration providerReminderConfiguration = new ProviderReminderConfiguration();
        providerReminderConfiguration.setDayOfWeek(dayOfWeek);
        providerReminderConfiguration.setHour(hour);
        providerReminderConfiguration.setMinute(minute);
        providerReminderConfiguration.setScheduled(scheduled);
        providerReminderConfiguration.setReminderType(type);
        return providerReminderConfiguration;
    }
}