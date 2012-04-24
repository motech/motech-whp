package org.motechproject.whp.validation.validator;

import org.motechproject.whp.validation.constraints.Enumeration;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnumValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        if (field.isAnnotationPresent(Enumeration.class)) {
            Class<? extends Enum> possibleValues = field.getAnnotation(Enumeration.class).type();
            List<String> allEnumerations = allEnumValues(possibleValues);
            if (isInValid(target, field, allEnumerations)) {
                errors.rejectValue(field.getName(), "invalid-data", "The value should be one of : " + allEnumerations);
            }
        }
    }

    private boolean isInValid(Object target, Field field, List<String> allEnumerations) {
        field.setAccessible(true);
        try {
            return !allEnumerations.contains(field.get(target).toString());
        } catch (Exception ignored) {
        }
        return true;
    }

    private List<String> allEnumValues(Class<? extends Enum> enumerationClass) {
        List<String> allEnumerations = new ArrayList<String>();
        for (Enum value : enumerationClass.getEnumConstants()) {
            allEnumerations.add(value.name());
        }
        return allEnumerations;
    }

}
