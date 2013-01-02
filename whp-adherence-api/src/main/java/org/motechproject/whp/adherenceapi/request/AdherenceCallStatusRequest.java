package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.whp.common.validation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_call_status_request")
@Setter
@EqualsAndHashCode
public class AdherenceCallStatusRequest implements Serializable {

    @NotBlank
    private String providerId;
    @NotBlank
    private String callAnswered;
    @NotBlank
    private String callId;
    @NotBlank
    private String flashingCallId;
    @DateTimeFormat
    private String attemptTime;
    @DateTimeFormat
    private String startTime;
    @DateTimeFormat
    private String endTime;
    @NotBlank
    private String callStatus;
    @NotBlank
    private String disconnectionType;
    @NotBlank
    @Digits(message = "should be a number", integer = 10, fraction = 0)
    private String patientCount;
    @NotBlank
    @Digits(message = "should be a number", integer = 10, fraction = 0)
    private String adherenceCapturedCount;
    @NotBlank
    @Digits(message = "should be a number", integer = 10, fraction = 0)
    private String adherenceNotCapturedCount;

    public AdherenceCallStatusRequest() {
    }

    @XmlElement(name = "provider_id")
    public String getProviderId() {
        return providerId;
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "flashing_call_id")
    public String getFlashingCallId() {
        return flashingCallId;
    }

    @XmlElement(name = "attempt_time")
    public String getAttemptTime() {
        return attemptTime;
    }

    @XmlElement(name = "start_time")
    public String getStartTime() {
        return startTime;
    }

    @XmlElement(name = "end_time")
    public String getEndTime() {
        return endTime;
    }

    @XmlElement(name = "call_status")
    public String getCallStatus() {
        return callStatus;
    }

    @XmlElement(name = "call_answered")
    public String getCallAnswered() {
        return callAnswered;
    }

    @XmlElement(name = "disconnection_type")
    public String getDisconnectionType() {
        return disconnectionType;
    }

    @XmlElement(name = "patient_count")
    public String getPatientCount() {
        return patientCount;
    }

    @XmlElement(name = "adherence_captured_count")
    public String getAdherenceCapturedCount() {
        return adherenceCapturedCount;
    }

    @XmlElement(name = "adherence_not_captured_count")
    public String getAdherenceNotCapturedCount() {
        return adherenceNotCapturedCount;
    }
}
