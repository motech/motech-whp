package org.motechproject.whp.controller;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
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
        Date today = new Date();

        when(providerReminderScheduler.getNextFireTime(ADHERENCE_WINDOW_APPROACHING)).thenReturn(today);

        standaloneSetup(providerReminderController)
                .build()
                .perform(get("/providerreminder/schedule/" + ADHERENCE_WINDOW_APPROACHING.name()))
                .andExpect(content().string(WHPDateTime.date(new DateTime(today)).value()))
                .andExpect(status().isOk());
    }

    @Test
    @Ignore("Work in progress")
    public void shouldUpdateSchedule() throws Exception {
        Date today = new Date();

        standaloneSetup(providerReminderController)
                .build()
                .perform(get("/providerreminder/schedule/" + ADHERENCE_WINDOW_APPROACHING.name() + "/update").param("weekDay", "SUN").param("hour", "10").param("minute", "30"))
                .andExpect(content().string(WHPDateTime.date(new DateTime(today)).value()))
                .andExpect(status().isOk());
    }
}
