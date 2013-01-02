package org.motechproject.whp.common.validation;

import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeValidator implements ConstraintValidator<DateTimeFormat, String> {

    public static final String DATE_TIME_FORMAT = "dd/MM/ HH:mm:ss";

    @Override
    public void initialize(DateTimeFormat dateTimeFormat) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        } else {
            DateTimeFormatter formatter = buildFormatter();
            try {
                formatter.parseDateTime(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private DateTimeFormatter buildFormatter() {
        DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2);
        formatterBuilder.appendLiteral('/');
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2);
        formatterBuilder.appendLiteral('/');
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.year(), 4);
        formatterBuilder.appendLiteral(' ');
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2);
        formatterBuilder.appendLiteral(':');
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2);
        formatterBuilder.appendLiteral(':');
        formatterBuilder.appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2);
        return formatterBuilder.toFormatter();
    }
}
