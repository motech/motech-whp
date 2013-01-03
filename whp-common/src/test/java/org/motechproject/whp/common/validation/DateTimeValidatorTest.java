package org.motechproject.whp.common.validation;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DateTimeValidatorTest {

    private DateTimeValidator dateTimeValidator;

    @Before
    public void setup() {
        dateTimeValidator = new DateTimeValidator();
    }

    @Test
    public void shouldBeValidForBlankValuesWhenAllowBlankIsSet() throws Exception {
        TestDateTime allowBlank = new TestDateTime();
        dateTimeValidator.initialize(allowBlank.getClass().getField("blank").getAnnotation(DateTimeFormat.class));
        assertTrue(dateTimeValidator.isValid("", null));
    }

    @Test
    public void shouldBeValidWhenValueIsNullAndAllowBlankIsSet() throws Exception {
        TestDateTime allowBlank = new TestDateTime();
        dateTimeValidator.initialize(allowBlank.getClass().getField("blank").getAnnotation(DateTimeFormat.class));
        assertTrue(dateTimeValidator.isValid(null, null));
    }

    @Test
    public void shouldNotBeValidOnNullWhenAllowBlankIsNotSet() throws Exception {
        TestDateTime dontAllowBlank = new TestDateTime();
        dateTimeValidator.initialize(dontAllowBlank.getClass().getField("dontAllowBlank").getAnnotation(DateTimeFormat.class));
        assertFalse(dateTimeValidator.isValid(null, null));
    }

    @Test
    public void shouldNotBeValidOnEmptyStringWhenAllowBlankIsNotSet() throws Exception {
        TestDateTime dontAllowBlank = new TestDateTime();
        dateTimeValidator.initialize(dontAllowBlank.getClass().getField("dontAllowBlank").getAnnotation(DateTimeFormat.class));
        assertFalse(dateTimeValidator.isValid("", null));
    }

    @Test
    public void shouldBeValidOnValueWithValidFormat() {
        assertTrue(dateTimeValidator.isValid("31/12/2012 10:10:10", null));
    }

    @Test
    public void shouldBeInvalidOnAnInvalidCalendarDate() {
        assertFalse(dateTimeValidator.isValid("31/02/2012 10:10:10", null));
    }

    @Test
    public void shouldBeInvalidWhenYearIsLessThanFourDigits() {
        assertFalse(dateTimeValidator.isValid("28/02/202 10:10:10", null));
    }

    @Test
    public void shouldBeInvalidOnValueWithInvalidFormat() {
        assertFalse(dateTimeValidator.isValid("12/02/2012 10:10:", null));
    }

    @Data
    private static class TestDateTime {
        @DateTimeFormat(allowBlank = true)
        public String blank;
        @DateTimeFormat(allowBlank = false)
        public String dontAllowBlank;
    }
}
