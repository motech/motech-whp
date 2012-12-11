package org.motechproject.whp.common.validation;

import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.common.util.WHPDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeValidator implements ConstraintValidator<DateTimeFormat, String> {

    @Override
    public void initialize(DateTimeFormat dateTimeFormat) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter formatter = org.joda.time.format.DateTimeFormat.forPattern(WHPDate.DATE_TIME_FORMAT);
        try {
            formatter.parseDateTime(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
