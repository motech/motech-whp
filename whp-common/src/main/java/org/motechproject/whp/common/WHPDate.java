package org.motechproject.whp.common;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WHPDate {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern(WHPConstants.DATE_FORMAT);

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
        return date.toString(WHPConstants.DATE_FORMAT);
    }

    public String lucidValue(){
        if (null == date) {
            return "";
        }
        return date.toString(WHPConstants.LUCID_DATE_FORMAT);
    }

    public LocalDate date() {
        return date;
    }
}
