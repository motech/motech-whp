package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.v0.exception.WHPErrorCodeV0;

import java.util.List;

@Data
public class WeightStatisticsRecordV0 {

    private WeightInstanceV0 weight_instance;
    private Double weight;
    private LocalDate measuringDate;

    //Required for Ektorp
    public WeightStatisticsRecordV0() {
    }

    public WeightStatisticsRecordV0(WeightInstanceV0 weightInstance, Double weight, LocalDate measuringDate) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCodeV0> validationErrors) {
        boolean isFilled = weight_instance != null && weight != null && measuringDate != null;
        if (!isFilled) {
            validationErrors.add(WHPErrorCodeV0.NULL_VALUE_IN_WEIGHT_STATISTICS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weight_instance == null && weight == null;
    }

    @JsonIgnore
    public boolean isOfInstance(WeightInstanceV0 weightInstance) {
        return this.weight_instance.equals(weightInstance);
    }
}