package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@EqualsAndHashCode
@XmlSeeAlso({ValidRangeError.class})
public class AdherenceResponseError {

    @XmlElement(name = "error_code")
    private String errorCode;

    protected AdherenceResponseError() {
    }

    public AdherenceResponseError(String errorCode) {
        this.errorCode = errorCode;
    }
}
