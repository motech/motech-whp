package org.motechproject.whp.common.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnumValidatorTest {

    @Test
    public void shouldBeValidIfEnumValueIsThere() throws Exception {
        EnumValidator enumValidator = new EnumValidator();
        enumValidator.initialize(MyEnumeration.class.getField("myField").getAnnotation(Enumeration.class));
        assertFalse(enumValidator.isValid("something", null));
        assertTrue(enumValidator.isValid("MY_VALUE_1", null));
    }

    private static class MyEnumeration {

        @Enumeration(TestEnum.class)
        public String myField;

        private MyEnumeration(String myField) {
            this.myField = myField;
        }
    }

    public enum TestEnum {

        MY_VALUE_1, MY_VALUE_2;
    }
}

