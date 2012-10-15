package org.motechproject.whp.wgninbound.request;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProviderVerificationRequestTest {

    private ProviderVerificationRequest request;

    @Before
    public void setup() {
        request = new ProviderVerificationRequest();
    }

    @Test
    public void shouldReturnLastTenDigitsAsPhoneNumberForNumberOfLengthTen() {
        request.setMsisdn("1234567890");
        assertEquals("1234567890", request.getPhoneNumber());
    }

    @Test
    public void shouldReturnLastTenDigitsAsPhoneNumberForNumberOfLengthGreaterThanTen() {
        request.setMsisdn("12345678901");
        assertEquals("2345678901", request.getPhoneNumber());
    }
}
