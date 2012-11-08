package org.motechproject.whp.request;

import lombok.Setter;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallDetailsLogRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement(name = "call_log")
public class IvrContainerRegistrationCallLogRequest {
    private String callId;
    private String startTime;
    private String endTime;
    private String disconnectionType;
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

    public ContainerRegistrationCallDetailsLogRequest mapToContainerRegistrationCallLogRequest(String providerId) {
        ContainerRegistrationCallDetailsLogRequest callLogRequest = new ContainerRegistrationCallDetailsLogRequest();
        callLogRequest.setCallId(callId);
        callLogRequest.setDisconnectionType(disconnectionType);
        callLogRequest.setMobileNumber(mobileNumber);
        callLogRequest.setStartDateTime(startTime);
        callLogRequest.setEndDateTime(endTime);
        callLogRequest.setProviderId(providerId);
        return callLogRequest;
    }
}
