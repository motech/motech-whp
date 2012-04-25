package org.motechproject.whp.validation.validator;

import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.validation.validator.root.PropertyValidator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

@Component
public class DateFieldValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        if (field.isAnnotationPresent(DateTimeFormat.class)) {
            validateDateTimeFormat(target, field, errors);
        }
    }

    private void validateDateTimeFormat(Object target, Field field, Errors errors) {
        String expectedFormat = field.getAnnotation(DateTimeFormat.class).pattern();
        DateTimeFormatter localDateFormatter = org.joda.time.format.DateTimeFormat.forPattern(expectedFormat);
        field.setAccessible(true);
        try {
            localDateFormatter.parseLocalDate((String) field.get(target));
        } catch (IllegalAccessException e) {
            errors.rejectValue(field.getName(), "access-error", e.getStackTrace(), e.getMessage());
        } catch (IllegalArgumentException e) {
            errors.rejectValue(field.getName(), "datetime-format-error", e.getMessage());
        } catch (Exception e) {
            errors.rejectValue(field.getName(), "datetime-format-error", e.getMessage());
        }
    }
}
