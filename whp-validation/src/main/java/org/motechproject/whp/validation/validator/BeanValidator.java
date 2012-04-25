package org.motechproject.whp.validation.validator;

import org.motechproject.whp.validation.constraints.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class BeanValidator {

    protected Set<PropertyValidator> fieldValidators;

    @Autowired
    public BeanValidator(Set<PropertyValidator> fieldValidators) {
        this.fieldValidators = fieldValidators;
    }

    public void validate(Object target, String scope, Errors errors) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Scope.class)) {
                List<String> scopeList = Arrays.asList(field.getAnnotation(Scope.class).scope());
                if (scopeList.contains(scope)) {
                    validateField(target, scope, errors, field);
                }
            } else {
                validateField(target, scope, errors, field);
            }
        }
    }

    protected void validateField(Object target, String scope, Errors errors, Field field) {
        field.setAccessible(true);
        if (isValidatedMemberInstance(field)) {
            try {
                validateMemberInstance(target, scope, errors, field);
            } catch (IllegalAccessException ignored) {
            }
        } else {
            for (PropertyValidator fieldValidator : fieldValidators) {
                fieldValidator.validateField(target, field, errors);
            }
        }
    }

    private void validateMemberInstance(Object target, String scope, Errors errors, Field field) throws IllegalAccessException {
        Object fieldValue = field.get(target);
        errors.pushNestedPath(field.getName());
        this.validate(fieldValue, scope, errors);
        errors.popNestedPath();
    }

    private boolean isValidatedMemberInstance(Field field) {
        return field.isAnnotationPresent(Valid.class);
    }
}
