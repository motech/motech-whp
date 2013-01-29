package org.motechproject.whp.common.domain;

import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AllDaysOfWeekTest {
    @Test
    public void shouldReturnAlDaysOfWeek() {
        List<DayOfWeek> allDaysOfWeek = asList(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Thursday, DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Sunday);
        assertEquals(allDaysOfWeek, AllDaysOfWeek.allDaysOfWeek);
    }
}
