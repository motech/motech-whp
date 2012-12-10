package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;

@EqualsAndHashCode
class ValidAdherenceRange {

    @XmlElement(name = "treatment_category")
    private String treatmentCategory;

    @XmlElement(name = "valid_range_from")
    private String validRangeFrom;

    @XmlElement(name = "valid_range_to")
    private String validRangeTo;

    ValidAdherenceRange(String treatmentCategory, String validRangeFrom, String validRangeTo) {
        this.treatmentCategory = treatmentCategory;
        this.validRangeFrom = validRangeFrom;
        this.validRangeTo = validRangeTo;
    }
}
