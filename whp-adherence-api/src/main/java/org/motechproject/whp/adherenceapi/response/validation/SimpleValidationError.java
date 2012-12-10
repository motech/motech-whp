package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;

@EqualsAndHashCode
public class SimpleValidationError extends AdherenceValidationError {

    @XmlElement(name = "error_code")
    protected String errorCode;

    protected SimpleValidationError() {
    }

    public SimpleValidationError(String errorCode) {
        this.errorCode = errorCode;
    }
}
