package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.common.webservice.WebServiceResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_validation_response")
@EqualsAndHashCode
public class AdherenceValidationResponse implements Serializable {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;

    @XmlElement(name = "error_code")
    private String errorCode;

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
        response.result = WebServiceResponse.failure;
        return response;
    }

    public static AdherenceValidationResponse failure(Dosage dosage) {
        AdherenceValidationResponse response = failure();
        response.treatmentCategory = dosage.getTreatmentProvider().name();
        response.validRangeFrom = dosage.getValidRangeFrom();
        response.validRangeTo = dosage.getValidRangeTo();
        return response;
    }

    public static AdherenceValidationResponse failure(String errorCode) {
        AdherenceValidationResponse response = new AdherenceValidationResponse();
        response.result = WebServiceResponse.failure;
        response.errorCode = errorCode;
        return response;
    }
}
