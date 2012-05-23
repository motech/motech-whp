package org.motechproject.whp.util;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.util.WHPDateUtil;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class WHPDateUtilTest {

    @Test
    public void shouldReturnAllDatesInRange() {
        LocalDate startingOn = new LocalDate(2012, 5, 21);
        LocalDate asOf = new LocalDate(2012, 6, 1);

        LocalDate monday = new LocalDate(2012, 5, 21);
        LocalDate tuesday = new LocalDate(2012, 5, 22);
        LocalDate wednesday = new LocalDate(2012, 5, 23);
        LocalDate thursday = new LocalDate(2012, 5, 24);
        LocalDate friday = new LocalDate(2012, 5, 25);
        LocalDate saturday = new LocalDate(2012, 5, 26);
        LocalDate sunday = new LocalDate(2012, 5, 27);
        LocalDate nextMonday = new LocalDate(2012, 5, 28);
        LocalDate nextTuesday = new LocalDate(2012, 5, 29);
        LocalDate nextWednesday = new LocalDate(2012, 5, 30);
        LocalDate nextThursday = new LocalDate(2012, 5, 31);
        LocalDate nextFriday = new LocalDate(2012, 6, 1);

        assertEquals(asList(monday, tuesday, wednesday, thursday, friday, saturday, sunday, nextMonday, nextTuesday, nextWednesday, nextThursday, nextFriday),
                WHPDateUtil.getDatesInRange(startingOn, asOf));
    }
}
