package org.motechproject.whp.common.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class PhoneNumberTest {

    @Test
    public void shouldConsiderOnlyLastTenDigitsAsPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("01234567890");
        assertEquals("1234567890", phoneNumber.value());
    }

    @Test
    public void shouldBeNullWhenMSISDNIsNull() {
        PhoneNumber phoneNumber = new PhoneNumber(null);
        assertNull(phoneNumber.value());
    }

    @Test
    public void shouldBeNullWhenMSISDNLessThanTenDigits() {
        PhoneNumber phoneNumber = new PhoneNumber("123456789");
        assertNull(phoneNumber.value());
    }

    @Test
    public void shouldBeNullWhenMSISDNIsNotNumeric() {
        PhoneNumber phoneNumber = new PhoneNumber("a234567890");
        assertNull(phoneNumber.value());
    }
}
