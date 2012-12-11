package org.motechproject.whp.common.error;

import javax.xml.bind.annotation.XmlElement;

public class BindingError {

    @XmlElement(name = "field_name")
    private String fieldName;
    @XmlElement(name = "rejected_value")
    private String rejectedValue;
    @XmlElement(name = "message")
    private String message;

    private BindingError() {
    }

    public BindingError(String fieldName, String rejectedValue, String message) {
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
