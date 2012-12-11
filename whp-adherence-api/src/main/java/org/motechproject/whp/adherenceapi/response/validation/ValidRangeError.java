package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.Dosage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
public class ValidRangeError extends AdherenceValidationError {

    @XmlElement(name = "valid_adherence_range")
    private ValidAdherenceRange validAdherenceRange;

    protected ValidRangeError() {
    }

    public ValidRangeError(String errorCode, Dosage dosage) {
        super(errorCode);
        validAdherenceRange = new ValidAdherenceRange(dosage.getTreatmentProvider().name(), dosage.getValidRangeFrom(), dosage.getValidRangeTo());
    }
}
