package org.motechproject.whp.common.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WHPDate {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String LUCID_DATE_FORMAT = "EEEE, dd MMMM yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/YYYY HH:mm:ss";
    public static final LocalDate ARBITRARY_PAST_DATE =  new DateTime(1900, 1, 1, 0, 0, 0).toLocalDate();

    private DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

    private LocalDate date;

    public WHPDate() {
    }

    public WHPDate(LocalDate date) {
        this.date = date;
    }

    public WHPDate(String date) {
        if (StringUtils.isNotBlank(date))
            this.date = formatter.parseLocalDate(date);
    }

    public static WHPDate date(String date) {
        return new WHPDate(date);
    }

    public static WHPDate date(LocalDate date) {
        return new WHPDate(date);
    }

    public String value() {
        if (null == date) {
            return "";
        }
        return date.toString(DATE_FORMAT);
    }

    public String lucidValue(){
        if (null == date) {
            return "";
        }
        return date.toString(LUCID_DATE_FORMAT);
    }

    public LocalDate date() {
        return date;
    }
}
