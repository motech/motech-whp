package org.motechproject.whp.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.NoValidatorFoundException;
import org.motechproject.whp.validation.constraints.NamedConstraint;
import org.motechproject.whp.validation.constraints.Scope;
import org.motechproject.whp.validation.validator.root.NamedConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class AllNamedConstraintValidatorsTest extends SpringIntegrationTest {

    @Autowired
    BeanValidator validator;

    @Test
    public void shouldUseCustomNamedConstraintValidatorToValidateField() {
        ClassWithNamedConstraintValidations target = new ClassWithNamedConstraintValidations("bar");
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, "testClass");
        validator.validate(target, "create", result);

        assertEquals("value must be foo", result.getFieldError("fieldWithCustomConstraint").getDefaultMessage());
    }

    @Test(expected = NoValidatorFoundException.class)
    public void shouldThrowExceptionWhenNoNamedConstraintValidatorFoundForTheField() {
        ClassWithNamedConstraintValidations target = new ClassWithNamedConstraintValidations("bar");
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, "testClass");
        validator.validate(target, "invalidConstraint", result);
    }

    @Data
    public static class ClassWithNamedConstraintValidations {

        @NamedConstraint(name = "valueIsFoo")
        private String fieldWithCustomConstraint;

        @Scope(scope = "invalidConstraint")
        @NamedConstraint(name = "valueIsBar")
        private String fieldWithInvalidConstraint;

        public ClassWithNamedConstraintValidations(String value) {
            this.fieldWithCustomConstraint = value;
            this.fieldWithInvalidConstraint = "bar";
        }
    }

    @Component
    public static class FooValueValidator extends NamedConstraintValidator {

        @Override
        public String getConstraintName() {
            return "valueIsFoo";
        }

        @Override
        public void validateField(Object target, Field field, Errors errors) {
            try {
                if (!field.get(target).equals("foo")) {
                    errors.rejectValue(field.getName(), "value-error", "value must be foo");
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
