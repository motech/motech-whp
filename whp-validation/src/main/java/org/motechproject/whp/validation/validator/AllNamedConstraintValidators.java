package org.motechproject.whp.validation.validator;


import org.motechproject.whp.validation.NoValidatorFoundException;
import org.motechproject.whp.validation.constraints.NamedConstraint;
import org.motechproject.whp.validation.validator.root.NamedConstraintValidator;
import org.motechproject.whp.validation.validator.root.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class AllNamedConstraintValidators extends PropertyValidator {

    protected Map<String, NamedConstraintValidator> constraintValidators;

    @Autowired
    public AllNamedConstraintValidators(Set<NamedConstraintValidator> constraintValidators) {
        this.constraintValidators = new HashMap<String, NamedConstraintValidator>();
        for (NamedConstraintValidator constraintValidator : constraintValidators) {
            this.constraintValidators.put(constraintValidator.getConstraintName(), constraintValidator);
        }
    }

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        if (field.isAnnotationPresent(NamedConstraint.class)) {
            NamedConstraintValidator validator = getValidatorForField(field);
            validator.validateField(target, field, errors);
        }
    }

    private NamedConstraintValidator getValidatorForField(Field field) {
        String constraintName = field.getAnnotation(NamedConstraint.class).name();
        if (constraintValidators.containsKey(constraintName)) {
            return constraintValidators.get(constraintName);
        } else {
            throw new NoValidatorFoundException(constraintName);
        }
    }
}
