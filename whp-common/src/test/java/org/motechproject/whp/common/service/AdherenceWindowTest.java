package org.motechproject.whp.common.service;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceWindowTest {

    @Mock
    private AdherencePropertyValues adherenceProperties;
    private AdherenceWindow adherenceWindow;

    @Before
    public void setup() {
        initMocks(this);
        adherenceWindow = new AdherenceWindow(adherenceProperties);
    }

    @Test
    public void shouldBeValidAdherenceDayBasedOnCurrentDayOfWeek() {
        LocalDate sunday = new LocalDate(2012, 12, 2);

        when(adherenceProperties.validAdherenceDays()).thenReturn(asList(DayOfWeek.Sunday));

        assertTrue(adherenceWindow.isValidAdherenceDay(sunday));
    }

    @Test
    public void shouldNotBeValidAdherenceDayBasedOnCurrentDayOfWeek() {
        LocalDate monday = new LocalDate(2012, 12, 3);

        when(adherenceProperties.validAdherenceDays()).thenReturn(asList(DayOfWeek.Sunday));

        assertFalse(adherenceWindow.isValidAdherenceDay(monday));
    }

    @Test
    public void shouldBeValidAdherenceDayBasedOnOneOfTheValidAdherenceDaysConfigured() {
        LocalDate sunday = new LocalDate(2012, 12, 2);

        when(adherenceProperties.validAdherenceDays()).thenReturn(asList(DayOfWeek.Sunday, DayOfWeek.Monday));

        assertTrue(adherenceWindow.isValidAdherenceDay(sunday));
    }

    @Test
    public void shouldNotBeValidAdherenceDayBasedOnAllAdherenceDaysConfigured() {
        LocalDate tuesday = new LocalDate(2012, 12, 3);

        when(adherenceProperties.validAdherenceDays()).thenReturn(asList(DayOfWeek.Sunday, DayOfWeek.Monday));

        assertTrue(adherenceWindow.isValidAdherenceDay(tuesday));
    }
}
