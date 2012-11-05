package org.motechproject.whp.request;

import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallLogRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Setter
@XmlRootElement(name = "call_log")
public class IvrContainerRegistrationCallLogRequest {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    @NotBlank
    private String callId;
    @org.motechproject.validation.constraints.DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private String startTime;
    @org.motechproject.validation.constraints.DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private String endTime;
    @NotBlank
    private String disconnectionType;
    @NotBlank
    private String mobileNumber;

    @XmlElement(name = "call_id")
    public String getCallId() {
        return callId;
    }

    @XmlElement(name = "start_time")
    public String getStartTime() {
        return startTime;
    }

    @XmlElement(name = "end_time")
    public String getEndTime() {
        return endTime;
    }

    @XmlElement(name = "disconnect_type")
    public String getDisconnectionType() {
        return disconnectionType;
    }

    @XmlElement(name = "msisdn")
    public String getMobileNumber() {
        return mobileNumber;
    }

    public ContainerRegistrationCallLogRequest mapToContainerRegistrationCallLogRequest(String providerId) {
        ContainerRegistrationCallLogRequest callLogRequest = new ContainerRegistrationCallLogRequest();
        callLogRequest.setCallId(callId);
        callLogRequest.setDisconnectionType(disconnectionType);
        callLogRequest.setMobileNumber(mobileNumber);
        callLogRequest.setStartDateTime(toDate(startTime));
        callLogRequest.setEndDateTime(toDate(endTime));
        callLogRequest.setProviderId(providerId);
        return callLogRequest;
    }

    private Date toDate(String date) {
        return this.dateTimeFormatter.parseDateTime(date).toDate();
    }
}
