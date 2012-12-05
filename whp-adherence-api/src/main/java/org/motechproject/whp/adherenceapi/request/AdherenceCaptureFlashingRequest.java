package org.motechproject.whp.adherenceapi.request;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adherence_capture_flashing_request")
@Setter
public class AdherenceCaptureFlashingRequest {

    private String msisdn;

    private String callId;

    private String callTime;

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "call_time")
    public String getCallTime() {
        return callTime;
    }
}
