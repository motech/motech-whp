package org.motechproject.whp.wgninbound.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "validate_flashing_request")
@Setter
@EqualsAndHashCode
public class ProviderVerificationRequest {

    @NotNullOrEmpty
    @Size(min = 10, message = "should be atleast 10 dijits in length")
    private String msisdn;

    @NotNullOrEmpty
    private String call_id;

    private String time;

    /*Required for spring mvc*/
    public ProviderVerificationRequest() {
    }

    public ProviderVerificationRequest(String msisdn, String time, String call_id) {
        this.msisdn = msisdn;
        this.time = time;
        this.call_id = call_id;
    }

    public String getPhoneNumber() {
        int length = msisdn.length();
        return msisdn.substring(length - 10, length);
    }

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @XmlElement(name = "call_id")
    public String getCall_id() {
        return call_id;
    }

    @XmlElement(name = "time")
    public String getTime() {
        return time;
    }
}
