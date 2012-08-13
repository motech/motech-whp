package org.motechproject.whp.request;

import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.ivr.request.FlashingRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.joda.time.format.DateTimeFormat.forPattern;

@XmlRootElement(name = "missed_call")
@Setter
public class IvrFlashingWebRequest {

    private String mobileNumber;

    private String callTime;

    @XmlElement(name = "call_no")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @XmlElement(name = "time")
    public String getCallTime() {
        return callTime;
    }

    public FlashingRequest createFlashingRequest() {
        FlashingRequest flashingRequest = new FlashingRequest();
        flashingRequest.setMobileNumber(mobileNumber);
        flashingRequest.setCallTime(DateTime.parse(callTime, forPattern("dd/MM/yyyy HH:mm:ss")));
        return flashingRequest;
    }
}
