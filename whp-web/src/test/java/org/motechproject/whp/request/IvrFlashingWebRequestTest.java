package org.motechproject.whp.request;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.request.FlashingRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class IvrFlashingWebRequestTest {

    @Test
    public void shouldCreateFlashingRequest(){
        IvrFlashingWebRequest ivrFlashingWebRequest = new IvrFlashingWebRequest();
        String mobileNumber = "123456789";
        String callTime = "14/08/2012 11:20:59";
        ivrFlashingWebRequest.setMobileNumber(mobileNumber);
        ivrFlashingWebRequest.setCallTime(callTime);

        FlashingRequest expectedFlashingRequest = new FlashingRequest(mobileNumber, DateTime.parse(callTime, forPattern("dd/MM/yyyy HH:mm:ss")));

        FlashingRequest flashingRequest = ivrFlashingWebRequest.createFlashingRequest();

        assertThat(flashingRequest, is(expectedFlashingRequest));
    }

    @Test
    public void shouldTrimMobileNumberTo10Digits() {
        IvrFlashingWebRequest ivrFlashingWebRequest = new IvrFlashingWebRequest();
        String mobileNumber = "+911234567890";
        ivrFlashingWebRequest.setMobileNumber(mobileNumber);
        ivrFlashingWebRequest.setCallTime(DateUtil.now().toString(forPattern("dd/MM/yyyy HH:mm:ss")));

        FlashingRequest flashingRequest = ivrFlashingWebRequest.createFlashingRequest();
        assertThat(flashingRequest.getMobileNumber(), is("1234567890"));
    }
}
