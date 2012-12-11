package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.motechproject.whp.common.validation.DateTimeFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_capture_flashing_request")
@Setter
@EqualsAndHashCode
public class AdherenceFlashingRequest implements Serializable {

    @NotBlank
    @Length(min = 10)
    private String msisdn;

    @NotBlank
    private String callId;

    @NotBlank
    @DateTimeFormat
    private String callTime;

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return new PhoneNumber(msisdn).value();
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
