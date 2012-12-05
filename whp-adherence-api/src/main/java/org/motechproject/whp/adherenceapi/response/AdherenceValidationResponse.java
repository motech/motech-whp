package org.motechproject.whp.adherenceapi.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Data
public class AdherenceValidationResponse implements Serializable {
    private String result;
    private String treatmentCategory;
    private String validRangeFrom;
    private String validRangeTo;

    public AdherenceValidationResponse(String result) {
        this.result = result;
    }
}
