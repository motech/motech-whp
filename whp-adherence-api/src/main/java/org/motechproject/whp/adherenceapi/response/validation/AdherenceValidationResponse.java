package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.common.webservice.WebServiceResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static org.motechproject.whp.adherenceapi.response.AdherenceIVRError.INVALID_ADHERENCE;

@XmlRootElement(name = "adherence_validation_response")
@EqualsAndHashCode
public class AdherenceValidationResponse implements Serializable {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;

    @XmlElement(name = "error")
    private AdherenceValidationError error;

    public static AdherenceValidationResponse success() {
        return new AdherenceValidationResponse();
    }

    public static AdherenceValidationResponse failure() {
        AdherenceValidationResponse response = new AdherenceValidationResponse();
        response.result = WebServiceResponse.failure;
        return response;
    }

    public static AdherenceValidationResponse failure(Dosage dosage) {
        AdherenceValidationResponse response = failure();
        response.error = new ValidRangeError(INVALID_ADHERENCE.name(), dosage);
        return response;
    }

    public static AdherenceValidationResponse failure(String errorCode) {
        AdherenceValidationResponse response = failure();
        response.error = new SimpleValidationError(errorCode);
        return response;
    }
}
