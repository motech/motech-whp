package org.motechproject.whp.validation.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.Set;

@Component
public class JSRConstraintsValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        Set<ConstraintViolation<Object>> constraintViolations = this.validateProperty(
                target,
                field.getName()
        );
        setErrors(field, constraintViolations, errors);
    }

    protected void setErrors(Field field, Set<ConstraintViolation<Object>> constraintViolations, Errors errors) {
        for (ConstraintViolation<Object> violation : constraintViolations) {
            errors.rejectValue(field.getName(), "format-error", violation.getMessage());
        }
    }
}
