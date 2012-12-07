package org.motechproject.whp.adherenceapi.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
}
