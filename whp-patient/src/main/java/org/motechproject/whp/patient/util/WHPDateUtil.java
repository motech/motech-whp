package org.motechproject.whp.patient.util;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.motechproject.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class WHPDateUtil {

    public static List<LocalDate> getDatesInRange(LocalDate startingOn, LocalDate asOf) {
        List<LocalDate> datesAcrossCurrentWeek = new ArrayList<LocalDate>();
        for (LocalDate date = startingOn; isOnOrBefore(date, asOf); date = date.plusDays(1)) {
            datesAcrossCurrentWeek.add(date);
        }
        return datesAcrossCurrentWeek;
    }

    public static boolean isOnOrAfter(LocalDate isThisDate, LocalDate onOrAfterThisDate) {
        return isThisDate.equals(onOrAfterThisDate) || isThisDate.isAfter(onOrAfterThisDate);
    }

    public static boolean isOnOrBefore(LocalDate isThisDate, LocalDate onOrBeforeThisDate) {
        return isThisDate.equals(onOrBeforeThisDate) || isThisDate.isBefore(onOrBeforeThisDate);
    }

    public static int findNumberOfDays(int monthOfYear, int year) {
        if ((monthOfYear < 8 && monthOfYear % 2 != 0) || (monthOfYear > 7 && monthOfYear % 2 == 0)) return 31;
        else if (monthOfYear == 2) {
            if (year % 4 == 0 && year % 400 != 0) return 29;
            else return 28;
        } else return 30;
    }

    public static int weeksElapsedSince(LocalDate date) {
        return Weeks.weeksBetween(date, DateUtil.today()).getWeeks();
    }
}
