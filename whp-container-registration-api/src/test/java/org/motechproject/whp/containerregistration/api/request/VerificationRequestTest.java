package org.motechproject.whp.containerregistration.api.request;

import org.junit.Test;
import org.motechproject.whp.containerregistration.api.request.VerificationRequest;

import static org.junit.Assert.assertEquals;

public class VerificationRequestTest {

    private VerificationRequest request;

    @Test
    public void shouldReturnLastTenDigitsAsPhoneNumberForNumberOfLengthTen() {
        request = new VerificationRequest("1234567890");
        assertEquals("1234567890", request.getPhoneNumber());
    }

    @Test
    public void shouldReturnLastTenDigitsAsPhoneNumberForNumberOfLengthGreaterThanTen() {
        request = new VerificationRequest("2345678901");
        assertEquals("2345678901", request.getPhoneNumber());
    }
}
