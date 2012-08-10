package org.motechproject.whp.webservice.contract;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "missed_call")
@Setter
public class FlashingRequest {

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
}
