package org.motechproject.whp.mapping;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
            return "dd/MM/yyyy";
        else
            return "dd/MM/yyyy HH:mm:ss";
    }
}
