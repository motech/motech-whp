package org.motechproject.whp.providerreminder.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderReminderPropertiesTest {

    @Mock
    private Properties providerReminderProperty;
    private ProviderReminderProperties providerReminderProperties;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderProperties = new ProviderReminderProperties(providerReminderProperty);
    }

    @Test
    public void shouldGetWeekdayFromPropertyFile() {
        when(providerReminderProperty.getProperty("weekday")).thenReturn("MON");

        String weekDay = providerReminderProperties.getWeekDay();

        assertEquals("MON", weekDay);
    }

    @Test
    public void shouldGetHourFromPropertyFile() {
        when(providerReminderProperty.getProperty("hour")).thenReturn("17");

        String hour = providerReminderProperties.getHour();

        assertEquals("17", hour);
    }

    @Test
    public void shouldGetMinuteFromPropertyFile() {
        when(providerReminderProperty.getProperty("minutes")).thenReturn("20");

        String minutes = providerReminderProperties.getMinutes();

        assertEquals("20", minutes);
    }
}
