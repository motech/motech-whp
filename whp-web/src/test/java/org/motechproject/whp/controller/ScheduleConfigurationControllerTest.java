package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.providerreminder.model.ScheduleConfiguration;
import org.motechproject.whp.providerreminder.service.WHPSchedulerService;

import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.Sunday;
import static org.motechproject.whp.providerreminder.domain.ScheduleType.ADHERENCE_WINDOW_COMMENCED;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ScheduleConfigurationControllerTest {

    @Mock
    private WHPSchedulerService WHPSchedulerService;
    @Mock
    private EventContext eventContext;

    private ScheduleConfigurationController scheduleConfigurationController;

    @Before
    public void setUp() {
        initMocks(this);
        scheduleConfigurationController = new ScheduleConfigurationController(eventContext, WHPSchedulerService);
    }

    @Test
    public void shouldReturnNextScheduleTiming() throws Exception {
        ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration(ADHERENCE_WINDOW_COMMENCED, new Date());

        when(WHPSchedulerService.configuration(ADHERENCE_WINDOW_COMMENCED)).thenReturn(scheduleConfiguration);

        standaloneSetup(scheduleConfigurationController)
                .build()
                .perform(get("/schedule/" + ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(model().attribute("scheduleConfiguration", scheduleConfiguration))
                .andExpect(view().name("schedule/scheduleConfiguration"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRemindProvidersOnAdherenceWindowCommenced() throws Exception {
        standaloneSetup(scheduleConfigurationController)
                .build()
                .perform(get("/schedule/execute").param("type", ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(content().string("Triggered reminder"))
                .andExpect(status().isOk());
        verify(eventContext).send(ADHERENCE_WINDOW_COMMENCED.getEventSubject());
    }

    @Test
    public void shouldUpdateSchedule() throws Exception {
        standaloneSetup(scheduleConfigurationController)
                .build()
                .perform(post("/schedule/update").param("dayOfWeek", "Sunday").param("hour", "10").param("minute", "30").param("reminderType", ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(view().name("redirect:/schedule/" + ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(status().isOk());

        ScheduleConfiguration expectedReminderConfiguration = new ScheduleConfiguration();
        expectedReminderConfiguration.setDayOfWeek(Sunday);
        expectedReminderConfiguration.setHour(10);
        expectedReminderConfiguration.setMinute(30);
        expectedReminderConfiguration.setReminderType(ADHERENCE_WINDOW_COMMENCED);

        verify(WHPSchedulerService).scheduleReminder(expectedReminderConfiguration);
    }

    @Test
    public void shouldUnScheduleReminder() throws Exception {
        standaloneSetup(scheduleConfigurationController)
                .build()
                .perform(post("/schedule/update/unschedule").param("dayOfWeek", "Sunday").param("hour", "10").param("minute", "30").param("reminderType", ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(view().name("redirect:/schedule/" + ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(status().isOk());

        ScheduleConfiguration expectedReminderConfiguration = new ScheduleConfiguration();
        expectedReminderConfiguration.setDayOfWeek(Sunday);
        expectedReminderConfiguration.setHour(10);
        expectedReminderConfiguration.setMinute(30);
        expectedReminderConfiguration.setReminderType(ADHERENCE_WINDOW_COMMENCED);

        verify(WHPSchedulerService).unScheduleReminder(expectedReminderConfiguration);
    }
}
