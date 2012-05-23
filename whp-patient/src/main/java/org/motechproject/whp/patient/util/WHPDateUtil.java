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
}
