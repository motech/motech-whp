package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreatmentWeekTest {

    @Test
    public void startDateShouldBeMonday(){
        LocalDate saturday = new LocalDate(2012, 5, 5);
        LocalDate monday = new LocalDate(2012, 4, 30);
        assertEquals(monday, new TreatmentWeek(saturday).startDate());
    }

    @Test
    public void endDateShouldBeSunday(){
        LocalDate monday = new LocalDate(2012, 4, 30);
        LocalDate sunday = new LocalDate(2012, 5, 6);
        assertEquals(sunday, new TreatmentWeek(monday).endDate());
    }
}
