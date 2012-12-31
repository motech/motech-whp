package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

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
    private String providerId;
    @NotBlank
    private String callId;
    @NotBlank
    private String ivrFileLength;
    @NotBlank
    private String timeTaken;

    public AdherenceValidationRequest() {
    }

    public AdherenceValidationRequest(String patientId) {
        this.patientId = patientId;
    }

    @XmlElement(name = "patient_id")
    public String getPatientId() {
        return patientId;
    }

    @XmlElement(name = "adherence_value")
    public String getDoseTakenCount() {
        return doseTakenCount;
    }

    @XmlElement(name = "provider_id")
    public String getProviderId() {
        return providerId;
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "time_taken")
    public String getTimeTaken() {
        return timeTaken;
    }

    @XmlElement(name = "ivr_file_length")
    public String getIvrFileLength() {
        return ivrFileLength;
    }

    public int doseTakenCount() {
        return Integer.parseInt(doseTakenCount);
    }
}
