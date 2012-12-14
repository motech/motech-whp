package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.common.domain.PhoneNumber;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_confirmation_request")
@Setter
@EqualsAndHashCode
public class AdherenceConfirmationRequest implements Serializable {
    @NotBlank
    private String patientId;
    @NotBlank
    private String doseTakenCount;
    @NotBlank
    @Length(min = 10)
    private String msisdn;
    @NotBlank
    private String callId;
    @NotBlank
    private String timeTaken;

    public AdherenceConfirmationRequest() {
    }

    public AdherenceConfirmationRequest(String patientId) {
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

    public AdherenceValidationRequest validationRequest() {
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setCallId(callId);
        validationRequest.setDoseTakenCount(doseTakenCount);
        validationRequest.setMsisdn(msisdn);
        validationRequest.setPatientId(patientId);
        validationRequest.setTimeTaken(timeTaken);
        return validationRequest;
    }
}
