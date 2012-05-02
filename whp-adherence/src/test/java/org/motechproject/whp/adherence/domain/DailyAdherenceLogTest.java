package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;

import static junit.framework.Assert.assertEquals;

public class DailyAdherenceLogTest extends BaseUnitTest {

    @Test
    public void shouldSetThePillDateWhenPillDayIsExactly1WeekAgo() {
        mockCurrentDate(new LocalDate(2012, 5, 2));
        DailyAdherenceLog adherenceLog = new DailyAdherenceLog(DayOfWeek.Monday);
        assertEquals(new LocalDate(2012, 4, 23), adherenceLog.getPillDate());
    }

    @Test
    public void shouldSetThePillDateWhenPillDayIsMoreThanOneWeekAgo() {
        mockCurrentDate(new LocalDate(2012, 5, 4));
        DailyAdherenceLog adherenceLog = new DailyAdherenceLog(DayOfWeek.Monday);
        assertEquals(new LocalDate(2012, 4, 23), adherenceLog.getPillDate());
    }

}
