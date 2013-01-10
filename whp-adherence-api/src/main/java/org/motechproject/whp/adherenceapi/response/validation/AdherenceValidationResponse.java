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

    @XmlElement(name = "valid_adherence_range")
    private ValidAdherenceRange validAdherenceRange;

    @XmlElement(name = "error")
    private AdherenceResponseError error;

    public AdherenceValidationResponse(Dosage dosage) {
        if(dosage != null) {
            this.validAdherenceRange = new ValidAdherenceRange(dosage.getTreatmentProvider().name(), dosage.getValidRangeFrom(), dosage.getValidRangeTo());
        }
    }

    public AdherenceValidationResponse() {

    }

    public AdherenceValidationResponse success() {
        return this;
    }

    public AdherenceValidationResponse failure() {
        result = WebServiceResponse.failure;
        return this;
    }

    public AdherenceValidationResponse invalidAdherenceRange() {
        failure();
        error = new AdherenceResponseError(INVALID_ADHERENCE.name());
        return this;
    }

    public AdherenceValidationResponse failure(String errorCode) {
        failure();
        error = new AdherenceResponseError(errorCode);
        return this;
    }

    public boolean failed() {
        return (null != error) || (result == WebServiceResponse.failure);
    }
}
