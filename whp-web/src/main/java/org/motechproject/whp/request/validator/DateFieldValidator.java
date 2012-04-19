package org.motechproject.whp.request.validator;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.exception.WHPValidationException;

public class DateFieldValidator {

    private final DateTimeFormatter localDateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");

    public void validateLocalDate(String date, String fieldName) throws WHPValidationException {
        try {
            localDateFormatter.parseLocalDate(date);
        } catch (IllegalArgumentException e) {
            throw new WHPValidationException("Error in field:" + fieldName + ":" + e.getMessage());
        }
    }

    public void validateDateTime(String dateTime, String fieldName) {
        try {
            dateTimeFormatter.parseLocalDate(dateTime);
        } catch (IllegalArgumentException e) {
            throw new WHPValidationException("Error in field:" + fieldName + ":" + e.getMessage());
        }
    }

}