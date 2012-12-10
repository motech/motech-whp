package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.common.domain.PhoneNumber;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_validation_request")
@Setter
@EqualsAndHashCode
public class AdherenceValidationRequest implements Serializable {

    @NotBlank
    private String patientId;
    @NotBlank
    private String doseTakenCount;
    @NotBlank
    private String msisdn;
    @NotBlank
    private String callId;
    @NotBlank
    private String timeTaken;

    @XmlElement(name = "patient_id")
    public String getPatientId() {
        return patientId;
    }

    @XmlElement(name = "adherence_value")
    public String getDoseTakenCount() {
        return doseTakenCount;
    }

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return new PhoneNumber(msisdn).value();
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "time_taken")
    public String getTimeTaken() {
        return timeTaken;
    }

    public int doseTakenCount() {
        return Integer.parseInt(doseTakenCount);
    }
}
