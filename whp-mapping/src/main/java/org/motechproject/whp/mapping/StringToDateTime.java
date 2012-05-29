package org.motechproject.whp.mapping;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.whp.refdata.domain.WHPConstants;

public class StringToDateTime extends WHPCustomMapper {

    @Override
    public Object convert(Object src, Class<?> destClass) {
        if (destClass.equals(DateTime.class))
            return DateTime.parse((String) src, DateTimeFormat.forPattern(findFormat((String) src)));
        else
            return DateTime.parse((String) src, DateTimeFormat.forPattern(findFormat((String) src))).toLocalDate();
    }

    private String findFormat(String src) {
        if (src.split(" ").length == 1)
            return WHPConstants.DATE_FORMAT;
        else
            return WHPConstants.DATE_TIME_FORMAT;
    }
}
