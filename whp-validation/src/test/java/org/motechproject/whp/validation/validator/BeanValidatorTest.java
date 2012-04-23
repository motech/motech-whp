package org.motechproject.whp.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.constraints.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.constraints.NotNull;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class BeanValidatorTest extends SpringIntegrationTest {

    @Autowired
    BeanValidator beanValidator;

    @Test
    public void shouldValidateFieldUnderAllScopesWhenNotAnnotatedWithScope() {
        ClassWithValidations target = new ClassWithValidations(null, "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, ValidationScope.simpleUpdate, errors);

        assertEquals("may not be null", errors.getFieldError("fieldWithoutScope").getDefaultMessage());
    }

    @Test
    public void shouldNotValidateScopedFieldsUnderADifferentScope() {
        ClassWithValidations target = new ClassWithValidations("someValue", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, ValidationScope.simpleUpdate, errors);

        assertEquals(0, errors.getFieldErrors().size());
    }

    @Test
    public void shouldValidateScopedFieldUnderMatchingScope() {
        ClassWithValidations target = new ClassWithValidations("someValue", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, ValidationScope.create, errors);

        assertEquals("may not be null", errors.getFieldError("scopedField").getDefaultMessage());
    }

    @Data
    public static class ClassWithValidations {

        @NotNull
        private String fieldWithoutScope;

        @NotNull
        @Scope(scope = {ValidationScope.create})
        private String scopedField;

        public ClassWithValidations(String fieldWithoutScope, String scopedField) {
            this.fieldWithoutScope = fieldWithoutScope;
            this.scopedField = scopedField;
        }
    }

}
