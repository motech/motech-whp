package org.motechproject.whp.wgninbound.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.common.util.WHPDateTime;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "validate_flashing_request")
@Setter
@EqualsAndHashCode
public class ProviderVerificationRequest extends VerificationRequest {

    @NotNullOrEmpty
    @Size(min = 10, message = "should be atleast 10 digits in length")
    private String msisdn;

    @NotNullOrEmpty
    private String call_id;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = WHPDateTime.DATE_TIME_FORMAT)
    private String time;

    /*Required for spring mvc*/
    public ProviderVerificationRequest() {
    }

    public ProviderVerificationRequest(String msisdn, String time, String call_id) {
        super(msisdn);
        this.msisdn = msisdn;
        this.call_id = call_id;
        this.time = time;
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

    public void setMsisdn(String msisdn) {
        super.setMsisdn(msisdn);
        this.msisdn = msisdn;
    }
}
