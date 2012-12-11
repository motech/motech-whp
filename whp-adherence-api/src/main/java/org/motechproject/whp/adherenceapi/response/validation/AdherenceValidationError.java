package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@EqualsAndHashCode
@XmlSeeAlso({ValidRangeError.class})
public class AdherenceValidationError {

    @XmlElement(name = "error_code")
    private String errorCode;

    protected AdherenceValidationError() {
    }

    public AdherenceValidationError(String errorCode) {
        this.errorCode = errorCode;
    }
}
