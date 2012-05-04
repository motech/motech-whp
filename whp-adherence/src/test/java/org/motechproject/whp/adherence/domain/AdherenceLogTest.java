package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import static junit.framework.Assert.assertEquals;

public class AdherenceLogTest {

    @Test
    public void shouldRecordAdherenceForPreviousWeek() {
        LocalDate wednesday = new LocalDate(2012, 5, 2);
        LocalDate sevenDaysAgo = new LocalDate(2012, 4, 25);

        assertEquals(sevenDaysAgo, new AdherenceLog(DayOfWeek.Wednesday, wednesday).getPillDate());

    }

    @Test
    public void shouldRecordAdherenceForASmallerDayOfWeekInPreviousWeek() {
        LocalDate wednesday = new LocalDate(2012, 5, 2);
        LocalDate eightDaysAgo = new LocalDate(2012, 4, 24);

        assertEquals(eightDaysAgo, new AdherenceLog(DayOfWeek.Tuesday, wednesday).getPillDate());
    }

    @Test
    public void shouldRecordAdherenceForALargerDayOfWeekInPreviousWeek() {
        LocalDate wednesday = new LocalDate(2012, 5, 2);
        LocalDate sixDaysAgo = new LocalDate(2012, 4, 26);

        assertEquals(sixDaysAgo, new AdherenceLog(DayOfWeek.Thursday, wednesday).getPillDate());
    }

}
