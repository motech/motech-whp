package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_call_status_request")
@Setter
@EqualsAndHashCode
public class AdherenceCallStatusRequest implements Serializable {

    @NotBlank
    @Length(min = 10)
    private String msisdn;
    @NotBlank
    private String providerId;
    @NotBlank
    private String callId;
    @NotBlank
    private String flashingCallId;
    @NotBlank
    private String attemptTime;
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;
    @NotBlank
    private String callStatus;
    @NotBlank
    private String disconnectionType;
    @NotBlank
    private String patientCount;
    @NotBlank
    private String adherenceCapturedCount;

    public AdherenceCallStatusRequest() {
    }

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
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

    public AdherenceValidationRequest validationRequest() {
            return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
