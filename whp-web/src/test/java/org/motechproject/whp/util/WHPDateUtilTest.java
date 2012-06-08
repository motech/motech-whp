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

    @Test
    public void shouldReturnNumberOfDaysForGivenMonthInYear() {
        //feb in leap year
        assertEquals(29, WHPDateUtil.findNumberOfDays(2, 2012));
        //feb in non-leap year
        assertEquals(28, WHPDateUtil.findNumberOfDays(2, 2011));
        //feb in non-leap year, and round century
        assertEquals(28, WHPDateUtil.findNumberOfDays(2, 2000));

        //jan
        assertEquals(31, WHPDateUtil.findNumberOfDays(1, 2012));
        //march
        assertEquals(31, WHPDateUtil.findNumberOfDays(3, 2012));
        //april
        assertEquals(30, WHPDateUtil.findNumberOfDays(4, 2012));
        //may
        assertEquals(31, WHPDateUtil.findNumberOfDays(5, 2012));
        //june
        assertEquals(30, WHPDateUtil.findNumberOfDays(6, 2012));
        //july
        assertEquals(31, WHPDateUtil.findNumberOfDays(7, 2012));
        //august
        assertEquals(31, WHPDateUtil.findNumberOfDays(8, 2012));
        //september
        assertEquals(30, WHPDateUtil.findNumberOfDays(9, 2012));
        //october
        assertEquals(31, WHPDateUtil.findNumberOfDays(10, 2012));
        //november
        assertEquals(30, WHPDateUtil.findNumberOfDays(11, 2012));
        //december
        assertEquals(31, WHPDateUtil.findNumberOfDays(12, 2012));
    }
}
