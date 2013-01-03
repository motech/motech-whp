package org.motechproject.whp.providerreminder.model;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.wgn.outbound.WGNRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

@EqualsAndHashCode
@XmlRootElement(name = "provider_reminder_request")
public class ProviderReminderRequest implements WGNRequest {

    private String requestId;
    private ProviderReminderType reminderType;
    private List<String> msisdns;

    public ProviderReminderRequest() {
    }

    public ProviderReminderRequest(ProviderReminderType reminderType, List<String> msisdns, String requestId) {
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
