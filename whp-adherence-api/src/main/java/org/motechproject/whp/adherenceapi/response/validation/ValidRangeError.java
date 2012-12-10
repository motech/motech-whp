package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.Dosage;

import javax.xml.bind.annotation.XmlElement;

@EqualsAndHashCode
public class ValidRangeError extends SimpleValidationError {

    @XmlElement(name = "valid_adherence_range")
    protected ValidAdherenceRange validAdherenceRange;

    protected ValidRangeError() {
    }

    public ValidRangeError(String errorCode, Dosage dosage) {
        super(errorCode);
        validAdherenceRange = new ValidAdherenceRange(dosage.getTreatmentProvider().name(), dosage.getValidRangeFrom(), dosage.getValidRangeTo());
    }
}
