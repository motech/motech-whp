package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;

import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.Sunday;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_COMMENCED;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderReminderControllerTest {

    private ProviderReminderController providerReminderController;
    @Mock
    private ProviderReminderScheduler providerReminderScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderController = new ProviderReminderController(providerReminderScheduler);
    }

    @Test
    public void shouldReturnNextScheduleTiming() throws Exception {
        ProviderReminderConfiguration providerReminderConfiguration = new ProviderReminderConfiguration(ADHERENCE_WINDOW_COMMENCED, new Date());

        when(providerReminderScheduler.configuration(ADHERENCE_WINDOW_COMMENCED)).thenReturn(providerReminderConfiguration);

        standaloneSetup(providerReminderController)
                .build()
                .perform(get("/providerreminder/" + ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(model().attribute("providerReminderConfiguration", providerReminderConfiguration))
                .andExpect(view().name("reminders/providerReminder"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateSchedule() throws Exception {
        standaloneSetup(providerReminderController)
                .build()
                .perform(post("/providerreminder/update").param("dayOfWeek", "Sunday").param("hour", "10").param("minute", "30").param("reminderType", ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(view().name("redirect:/providerreminder/" + ADHERENCE_WINDOW_COMMENCED.name()))
                .andExpect(status().isOk());

        ProviderReminderConfiguration expectedReminderConfiguration = new ProviderReminderConfiguration();
        expectedReminderConfiguration.setDayOfWeek(Sunday);
        expectedReminderConfiguration.setHour(10);
        expectedReminderConfiguration.setMinute(30);
        expectedReminderConfiguration.setReminderType(ADHERENCE_WINDOW_COMMENCED);

        verify(providerReminderScheduler).scheduleReminder(expectedReminderConfiguration);
    }
}
