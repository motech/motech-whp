package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
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
    private String providerId;
    @NotBlank
    private String callId;
    @NotBlank
    private String timeTaken;
    @NotBlank
    private String ivrFileLength;

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

    public AdherenceValidationRequest validationRequest() {
        AdherenceValidationRequest validationRequest = new AdherenceValidationRequest();
        validationRequest.setCallId(callId);
        validationRequest.setProviderId(providerId);
        validationRequest.setPatientId(patientId);
        validationRequest.setTimeTaken(timeTaken);
        validationRequest.setIvrFileLength(ivrFileLength);
        return validationRequest;
    }
}
