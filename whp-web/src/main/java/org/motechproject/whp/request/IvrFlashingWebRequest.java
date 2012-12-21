package org.motechproject.whp.request;

import lombok.Setter;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.ivr.request.FlashingRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "missed_call")
@Setter
public class IvrFlashingWebRequest {

    private String mobileNumber;

    private String callTime;

    private String callId;

    @XmlElement(name = "call_no")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "time")
    public String getCallTime() {
        return callTime;
    }

    public FlashingRequest createFlashingRequest() {
        FlashingRequest flashingRequest = new FlashingRequest();
        flashingRequest.setMobileNumber(mobileNumber);
        flashingRequest.setCallTime(new WHPDateTime(callTime).dateTime());
        flashingRequest.setCallId(callId);
        return flashingRequest;
    }
}
