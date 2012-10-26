package org.motechproject.whp.common.mapping;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

public class StringToDateTime extends WHPCustomMapper {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Object convert(Object src, Class<?> destClass) {
        if(StringUtils.isEmpty((String) src))
            return null;
        try {
            if (destClass.equals(DateTime.class))
                return DateTime.parse((String) src, DateTimeFormat.forPattern(findFormat((String) src)));
            else
                return DateTime.parse((String) src, DateTimeFormat.forPattern(findFormat((String) src))).toLocalDate();
        }
        catch (IllegalArgumentException e){
            logger.error("Error occurred", e);
            return null;
        }
    }

    private String findFormat(String src) {
        if (src.split(" ").length == 1)
            return DATE_FORMAT;
        else
            return DATE_TIME_FORMAT;
    }
}
