package org.motechproject.whp.patient.util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.*;

public class WHPDateUtil {

    public static List<LocalDate> getDatesInRange(LocalDate startingOn, LocalDate asOf) {
        List<LocalDate> datesAcrossCurrentWeek = new ArrayList<LocalDate>();
        for (LocalDate date = startingOn; isOnOrBefore(newDateTime(date), newDateTime(asOf)); date = date.plusDays(1)) {
            datesAcrossCurrentWeek.add(date);
        }
        return datesAcrossCurrentWeek;
    }

    public static int findNumberOfDays(int monthOfYear, int year) {
        if ( (monthOfYear<8 && monthOfYear% 2!= 0) || (monthOfYear>7 && monthOfYear%2==0)) return 31;
        else if (monthOfYear == 2) {
            if (year % 4 == 0 && year % 400 != 0) return 29;
            else return 28;
        } else return 30;
    }
}
