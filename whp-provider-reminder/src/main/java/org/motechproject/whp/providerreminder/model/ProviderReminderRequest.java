package org.motechproject.whp.providerreminder.model;

import lombok.EqualsAndHashCode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode
@XmlRootElement(name = "provider_reminder_request")
public class ProviderReminderRequest implements Serializable {

    private String requestId;
    private String reminderType;
    private List<String> msisdns;

    public ProviderReminderRequest() {
    }

    public ProviderReminderRequest(String reminderType, List<String> msisdns, String requestId) {
        this.reminderType = reminderType;
        this.msisdns = msisdns;
        this.requestId = requestId;
    }

    @XmlElement(name = "reminder_type")
    public String getReminderType() {
        return reminderType;
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
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            JAXBContext.newInstance(ProviderReminderRequest.class).createMarshaller().marshal(this, outputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toString();
    }
}
