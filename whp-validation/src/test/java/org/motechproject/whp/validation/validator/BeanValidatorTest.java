package org.motechproject.whp.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.constraints.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.Valid;
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
        beanValidator.validate(target, "simpleUpdate", errors);

        assertEquals("may not be null", errors.getFieldError("fieldWithoutScope").getDefaultMessage());
    }

    @Test
    public void shouldNotValidateScopedFieldsUnderADifferentScope() {
        ClassWithValidations target = new ClassWithValidations("someValue", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, "simpleUpdate", errors);

        assertEquals(0, errors.getFieldErrors().size());
    }

    @Test
    public void shouldValidateScopedFieldUnderMatchingScope() {
        ClassWithValidations target = new ClassWithValidations("someValue", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, "create", errors);

        assertEquals("may not be null", errors.getFieldError("scopedField").getDefaultMessage());
    }

    @Test
    public void shouldValidateComposedFields() {
        ClassWithValidations target = new ClassWithValidations("someValue", new MemberField(null), "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        beanValidator.validate(target, "create", errors);

        assertEquals("may not be null", errors.getFieldError("memberField.notNullField").getDefaultMessage());
    }

    @Data
    public static class ClassWithValidations {

        @NotNull
        private String fieldWithoutScope;

        @Valid
        private MemberField memberField;

        @NotNull
        @Scope(scope = {"create"})
        private String scopedField;

        public ClassWithValidations(String fieldWithoutScope, String scopedField) {
            this(fieldWithoutScope, new MemberField("notNullValue"), scopedField);
        }

        public ClassWithValidations(String fieldWithoutScope, MemberField memberField, String scopedField) {
            this.fieldWithoutScope = fieldWithoutScope;
            this.memberField = memberField;
            this.scopedField = scopedField;
        }
    }

    public static class MemberField {

        @NotNull
        private String notNullField;

        public MemberField(String notNullField) {
            this.notNullField = notNullField;
        }

        public String getNotNullField() {
            return notNullField;
        }

        public void setNotNullField(String notNullField) {
            this.notNullField = notNullField;
        }
    }
}
