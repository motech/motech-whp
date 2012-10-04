package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.SampleInstance;

import java.io.Serializable;
import java.util.List;

@Data
public class WeightStatisticsRecord implements Serializable {

    private SampleInstance weight_instance;
    private Double weight;
    private LocalDate measuringDate;

    //Required for Ektorp
    public WeightStatisticsRecord() {
    }

    public WeightStatisticsRecord(SampleInstance SampleInstance, Double weight, LocalDate measuringDate) {
        this.weight_instance = SampleInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> validationErrors) {
        boolean isFilled = weight_instance != null && weight != null && measuringDate != null;
        if (!isFilled) {
            validationErrors.add(WHPErrorCode.NULL_VALUE_IN_WEIGHT_STATISTICS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weight_instance == null && weight == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SampleInstance SampleInstance) {
        return this.weight_instance.equals(SampleInstance);
    }
}
