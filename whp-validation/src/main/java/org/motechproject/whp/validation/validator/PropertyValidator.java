package org.motechproject.whp.validation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Field;

public abstract class PropertyValidator extends LocalValidatorFactoryBean {
    public abstract void validateField(Object target, Field field, Errors errors);
}
