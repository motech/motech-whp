package org.motechproject.whp.validation.validator.root;

import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Field;

public abstract class NamedConstraintValidator extends LocalValidatorFactoryBean {

    public abstract String getConstraintName();

    public abstract void validateField(Object target, Field field, Errors errors);
}
