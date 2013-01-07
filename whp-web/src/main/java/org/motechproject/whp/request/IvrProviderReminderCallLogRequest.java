package org.motechproject.whp.request;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.whp.reports.contract.ProviderReminderCallLogRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "provider_reminder_call_status")
@Setter
@EqualsAndHashCode
public class IvrProviderReminderCallLogRequest {

    private String requestId;
    private String callId;
    private String reminderId;
    private String reminderType;
    private String msisdn;
    private String callAnswered;
    private String disconnectionType;
    private String startTime;
    private String endTime;
    private String attempt;
    private String attemptTime;

    @XmlElement(name = "request_id")
    public String getRequestId() {
        return requestId;
    }

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "reminder_id")
    public String getReminderId() {
        return reminderId;
    }

    @XmlElement(name = "reminder_type")
    public String getReminderType() {
        return reminderType;
    }

    @XmlElement(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @XmlElement(name = "call_answered")
    public String getCallAnswered() {
        return callAnswered;
    }

    @XmlElement(name = "disconnection_type")
    public String getDisconnectionType() {
        return disconnectionType;
    }

    @XmlElement(name = "start_time", nillable = true)
    public String getStartTime() {
        return startTime;
    }

    @XmlElement(name = "end_time", nillable = true)
    public String getEndTime() {
        return endTime;
    }

    @XmlElement(name = "attempt")
    public String getAttempt() {
        return attempt;
    }

    @XmlElement(name = "attempt_time")
    public String getAttemptTime() {
        return attemptTime;
    }

    public ProviderReminderCallLogRequest mapToReportingRequest(String providerId) {
        ProviderReminderCallLogRequest request = new ProviderReminderCallLogRequest();
        request.setDisconnectionType(getDisconnectionType());
        request.setReminderType(getReminderType());
        request.setRequestId(getRequestId());
        request.setAttempt(getAttempt());
        request.setAttemptTime(getAttemptTime());
        request.setCallId(getCallId());
        request.setCallAnswered(getCallAnswered());
        request.setStartTime(getStartTime());
        request.setEndTime(getEndTime());
        request.setMsisdn(getMsisdn());
        request.setProviderId(providerId);
        return request;
    }
}
