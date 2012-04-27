package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.repository.ValidationErrors;

@Data
public class WeightStatistics {

    private WeightInstance weightInstance;
    private Double weight;
    private LocalDate measuringDate;

    public WeightStatistics() {
    }

    public WeightStatistics(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        this.weightInstance = weightInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        boolean isFilled = weightInstance != null && weight != null && measuringDate != null;
        if (!isFilled) {
            validationErrors.add("Invalid weight statistics : null value");
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weightInstance == null && weight == null && measuringDate == null;
    }

}
