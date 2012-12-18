package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.motechproject.whp.common.validation.Enumeration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_not_captured_request")
@Setter
@EqualsAndHashCode
public class AdherenceNotCapturedRequest implements Serializable {
    @NotBlank
    private String patientId;
    @NotBlank
    @Enumeration(AdherenceCaptureStatus.class)
    private String adherenceCaptureStatus;
    @NotBlank
    @Length(min = 10)
    private String msisdn;
    @NotBlank
    private String callId;
    @NotBlank
    private String timeTaken;

    public AdherenceNotCapturedRequest() {
    }

    public AdherenceNotCapturedRequest(String patientId) {
        this.patientId = patientId;
    }

    @XmlElement(name = "patient_id")
    public String getPatientId() {
        return patientId;
    }

    @XmlElement(name = "type")
    public String getAdherenceCaptureStatus() {
        return adherenceCaptureStatus;
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

    public AdherenceValidationRequest validationRequest() {
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setCallId(callId);
        validationRequest.setMsisdn(msisdn);
        validationRequest.setPatientId(patientId);
        validationRequest.setTimeTaken(timeTaken);
        return validationRequest;
    }
}
