package org.motechproject.whp.common.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.util.StringUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

public class WHPDateUtil {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

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

    public static int numberOf_DDD_Between(LocalDate startingOn, LocalDate asOf, DayOfWeek dayOfWeek){
        int count = 0;
        for (LocalDate date = startingOn; isOnOrBefore(date, asOf); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == dayOfWeek.getValue()) count++;
        }
        return count;
    }

    public static Date toDate(String date) {
        return StringUtil.isNullOrEmpty(date) ? null : dateTimeFormatter.parseDateTime(date).toDate();
    }

    public static java.sql.Date toSqlDate(LocalDate date){
        if(date == null)
            return null;
        return new java.sql.Date(date.toDate().getTime());
    }

    public static java.sql.Date toSqlDate(DateTime dateTime) {
        if(dateTime == null)
            return null;
        return new java.sql.Date(dateTime.toDate().getTime());
    }

    public static Timestamp toSqlTimestamp(DateTime dateTime) {
        if(dateTime == null)
            return null;
        return new Timestamp(dateTime.toDate().getTime());
    }

    public static Timestamp toSqlTimestamp(LocalDateTime dateTime) {
        if(dateTime == null)
            return null;
        return new Timestamp(dateTime.toDate().getTime());
    }

    public static DateTime toDateTime(String date) {
        if (date!= null){
            if(date.isEmpty()){
                return null;
            }
            else {
                String empty_time = " 00:00:00";
                return WHPDateTime.date(date+ empty_time).dateTime();
            }
        }
        return null;
    }
}
