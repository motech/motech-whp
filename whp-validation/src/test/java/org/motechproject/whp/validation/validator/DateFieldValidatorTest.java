package org.motechproject.whp.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.motechproject.whp.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class DateFieldValidatorTest extends SpringIntegrationTest {

    @Autowired
    DateFieldValidator dateFieldValidator;

    @Test
    public void shouldNotReportErrorForValidDateFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11/12/2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), errors);
        assertNull(errors.getFieldError("date"));
    }

    @Test
    public void shouldReportErrorForInValidDateFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("Invalid format: \"11-12-2007\" is malformed at \"-12-2007\"", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorForValidDateTimeFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11/12/2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), errors);
        assertNull(errors.getFieldError("dateTime"));
    }

    @Test
    public void shouldReportErrorForInValidDateTimeFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10-30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("Invalid format: \"11/12/2007 10-30:30\" is malformed at \"-30:30\"", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldReportErrorForNullFieldValues() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, null, null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), errors);
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), errors);
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateWithoutValidation"), errors);
        assertEquals(2, errors.getFieldErrors().size());
        assertEquals(null, errors.getFieldErrors().get(0).getDefaultMessage());
        assertEquals(null, errors.getFieldErrors().get(1).getDefaultMessage());
    }

    @Test
    public void shouldNotValidateFieldWithoutDateTimeFormatAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10-30:30", "11-12-2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateWithoutValidation"), errors);
        assertEquals(0, errors.getFieldErrors().size());
    }

    @Data
    public static class ClassWithValidations {

        @DateTimeFormat(pattern = "dd/MM/YYYY")
        private String date;

        @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm:ss")
        private String dateTime;

        private String dateWithoutValidation;

        public ClassWithValidations(String date, String dateTime, String dateWithoutValidation) {
            this.date = date;
            this.dateTime = dateTime;
            this.dateWithoutValidation = dateWithoutValidation;
        }
    }
}
