package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_validation_request")
@Setter
@EqualsAndHashCode
public class AdherenceValidationRequest implements Serializable {

    @NotNullOrEmpty
    private String patientId;
    @NotNullOrEmpty
    private String doseTakenCount;
    @NotNullOrEmpty
    private String msisdn;
    @NotNullOrEmpty
    private String callId;
    @NotNullOrEmpty
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
        return msisdn;
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
