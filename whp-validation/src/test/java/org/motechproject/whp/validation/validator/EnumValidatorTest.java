package org.motechproject.whp.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.motechproject.whp.repository.SpringIntegrationTest;
import org.motechproject.whp.validation.constraints.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class EnumValidatorTest extends SpringIntegrationTest {

    @Autowired
    EnumValidator enumValidator;

    @Test
    public void shouldReportErrorWhenValueIsNotEnumerated() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("notAnEnum", "enumValue1");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField"), errors);

        assertEquals("The value should be one of : [enumValue1, enumValue2]", errors.getFieldError("enumField").getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorWhenValueIsEnumerated() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("enumValue1", "enumValue1");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField"), errors);

        assertNull(errors.getFieldError("enumField"));
    }

    @Test
    public void shouldNotValidateFieldWithoutEnumerationAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("enumValue1", "notEnumeratedValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField"), errors);

        assertNull(errors.getFieldError("notValidated"));
    }

    @Data
    public static class ClassWithValidations {

        @Enumeration(type = Enum.class)
        private String enumField;

        private String notValidated;

        public ClassWithValidations(String enumField, String notValidated) {
            this.enumField = enumField;
            this.notValidated = notValidated;
        }
    }

    public enum Enum {
        enumValue1, enumValue2
    }

}
