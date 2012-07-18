package org.motechproject.whp.common.mapping;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

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
            return DATE_FORMAT;
        else
            return DATE_TIME_FORMAT;
    }
}
