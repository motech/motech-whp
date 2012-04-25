package org.motechproject.whp.validation.validator;

import org.motechproject.whp.validation.constraints.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

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
                    validateField(target, errors, field);
                }
            } else {
                validateField(target, errors, field);
            }
        }
    }

    private void validateField(Object target, Errors errors, Field field) {
        for (PropertyValidator fieldValidator : fieldValidators) {
            fieldValidator.validateField(target, field, errors);
        }
    }
}
