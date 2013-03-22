package org.motechproject.whp.it.schedule.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.motechproject.whp.schedule.repository.AllScheduleConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.whp.schedule.domain.ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllSchedulerConfigurationsIT extends SpringIntegrationTest {

    @Autowired
    AllScheduleConfigurations allScheduleConfigurations;

    @Before
    @After
    public void tearDown() {
        allScheduleConfigurations.removeAll();
    }

    @Test
    public void shouldSaveScheduleConfiguration() {
        ScheduleConfiguration scheduleConfiguration = configuration(DayOfWeek.Monday, 1, 1, PROVIDER_ADHERENCE_NOT_REPORTED, false);
        allScheduleConfigurations.saveOrUpdate(scheduleConfiguration);
        assertNotNull(allScheduleConfigurations.get(scheduleConfiguration.getId()));
    }

    @Test
    public void shouldNotMaintainDuplicateCopiesOfConfiguration() {
        ScheduleType type = PROVIDER_ADHERENCE_NOT_REPORTED;
        ScheduleConfiguration[] configurations = {configuration(DayOfWeek.Monday, 1, 1, type, false), configuration(DayOfWeek.Tuesday, 2, 2, type, true)};

        allScheduleConfigurations.saveOrUpdate(configurations[0]);
        allScheduleConfigurations.saveOrUpdate(configurations[1]);

        ScheduleConfiguration updatedConfiguration = allScheduleConfigurations.withType(type);
        assertEquals(configurations[1].getDayOfWeek(), updatedConfiguration.getDayOfWeek());
        assertEquals(configurations[1].getHour(), updatedConfiguration.getHour());
        assertEquals(configurations[1].getMinute(), updatedConfiguration.getMinute());
        assertEquals(configurations[1].isScheduled(), updatedConfiguration.isScheduled());
    }

    private ScheduleConfiguration configuration(DayOfWeek dayOfWeek, int hour, int minute, ScheduleType type, boolean scheduled) {
        ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration();
        scheduleConfiguration.setDayOfWeek(dayOfWeek);
        scheduleConfiguration.setHour(hour);
        scheduleConfiguration.setMinute(minute);
        scheduleConfiguration.setScheduled(scheduled);
        scheduleConfiguration.setScheduleType(type);
        return scheduleConfiguration;
    }
}