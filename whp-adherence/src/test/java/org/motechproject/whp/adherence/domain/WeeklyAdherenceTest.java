package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;

import static junit.framework.Assert.assertEquals;

public class WeeklyAdherenceTest extends BaseUnitTest {

    @Test
    public void shouldSetCurrentWeekAs_CurrentTreatmentWeek_GivenTodayIsSunday() {
        mockCurrentDate(new LocalDate(2012, 5, 27));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsMonday() {
        mockCurrentDate(new LocalDate(2012, 5, 28));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsSaturday() {
        mockCurrentDate(new LocalDate(2012, 5, 26));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 14), week.startDate());
        assertEquals(new LocalDate(2012, 5, 20), week.endDate());
    }
}
