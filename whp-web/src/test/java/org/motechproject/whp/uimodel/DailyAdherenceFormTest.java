package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;

import static junit.framework.Assert.assertEquals;

public class DailyAdherenceFormTest extends BaseUnitTest {

    @Test
    public void shouldSetThePillDateWhenPillDayIsExactly1WeekAgo() {
        mockCurrentDate(new LocalDate(2012, 5, 2));
        DailyAdherenceForm adherenceForm = new DailyAdherenceForm(DayOfWeek.Monday);
        assertEquals(new LocalDate(2012, 4, 23), adherenceForm.getPillDate());
    }

    @Test
    public void shouldSetThePillDateWhenPillDayIsMoreThanOneWeekAgo() {
        mockCurrentDate(new LocalDate(2012, 5, 4));
        DailyAdherenceForm adherenceForm = new DailyAdherenceForm(DayOfWeek.Monday);
        assertEquals(new LocalDate(2012, 4, 23), adherenceForm.getPillDate());
    }

}
