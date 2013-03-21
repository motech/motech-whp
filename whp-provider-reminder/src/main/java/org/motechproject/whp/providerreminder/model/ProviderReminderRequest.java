package org.motechproject.whp.providerreminder.model;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.wgn.outbound.WGNRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.StringWriter;
import java.util.List;

@EqualsAndHashCode
@XmlRootElement(name = "provider_reminder_request")
@XmlType(propOrder={"requestId", "reminderType", "msisdns"})
public class ProviderReminderRequest implements WGNRequest {

    private String requestId;
    private ScheduleType reminderType;
    private List<String> msisdns;

    public ProviderReminderRequest() {
    }

    public ProviderReminderRequest(ScheduleType reminderType, List<String> msisdns, String requestId) {
        this.reminderType = reminderType;
        this.msisdns = msisdns;
        this.requestId = requestId;
    }

    @XmlElement(name = "reminder_type")
    public String getReminderType() {
        return reminderType.name();
    }

    @XmlElement(name = "request_id")
    public String getRequestId() {
        return requestId;
    }

    @XmlElementWrapper(name = "msisdns")
    @XmlElement(name = "msisdn")
    public List<String> getMsisdns() {
        return msisdns;
    }

    public String toXML() {
        StringWriter writer =  new StringWriter();
        JAXB.marshal(this, writer);
        return writer.toString();
    }
}
