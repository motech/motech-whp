package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;
import org.motechproject.whp.common.domain.PhoneNumber;

import static junit.framework.Assert.assertEquals;

public class AdherenceCaptureFlashingRequestTest {

    @Test
    public void shouldReturnValidPhoneNumberAsValidMSISDN() {
        String msisdn = "1234567890";

        AdherenceCaptureFlashingRequest request = new AdherenceCaptureFlashingRequest();
        request.setMsisdn(msisdn);

        assertEquals(new PhoneNumber(msisdn).value(), request.getMsisdn());
    }
}
