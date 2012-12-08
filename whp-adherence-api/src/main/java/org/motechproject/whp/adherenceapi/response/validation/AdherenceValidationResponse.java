package org.motechproject.whp.adherenceapi.response.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.common.webservice.WebServiceResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_validation_response")
@EqualsAndHashCode
@Data
public class AdherenceValidationResponse implements Serializable {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;

    @XmlElement(name = "treatment_category")
    private String treatmentCategory;

    @XmlElement(name = "valid_range_from")
    private String validRangeFrom;

    @XmlElement(name = "valid_range_to")
    private String validRangeTo;

    public static AdherenceValidationResponse success() {
        return new AdherenceValidationResponse();
    }

    public static AdherenceValidationResponse failure() {
        AdherenceValidationResponse response = new AdherenceValidationResponse();
        response.setResult(WebServiceResponse.failure);
        return response;
    }

    public static AdherenceValidationResponse failure(Dosage dosage) {
        AdherenceValidationResponse response = failure();
        response.setTreatmentCategory(dosage.getTreatmentProvider().name());
        response.setValidRangeFrom(dosage.getValidRangeFrom());
        response.setValidRangeTo(dosage.getValidRangeTo());
        return response;
    }

}
