package org.motechproject.whp.common.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WHPDateTime {

    public static final String DATE_TIME_FORMAT = "dd/MM/YYYY HH:mm:ss";

    private DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    private DateTime date;

    public WHPDateTime() {
    }

    public WHPDateTime(DateTime date) {
        this.date = date;
    }

    public WHPDateTime(String date) {
        if (StringUtils.isNotBlank(date))
            this.date = formatter.parseDateTime(date);
    }

    public static WHPDateTime date(String date) {
        return new WHPDateTime(date);
    }

    public static WHPDateTime date(DateTime date) {
        return new WHPDateTime(date);
    }

    public String value() {
        if (null == date) {
            return "";
        }
        return date.toString(DATE_TIME_FORMAT);
    }

    public DateTime date() {
        return date;
    }
}
