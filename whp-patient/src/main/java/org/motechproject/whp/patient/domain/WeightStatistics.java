package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.repository.ValidationErrors;
import org.motechproject.whp.refdata.domain.WeightInstance;

@Data
public class WeightStatistics {

    private WeightInstance weight_instance;
    private Double weight;
    private LocalDate measuringDate;

    public WeightStatistics() {
    }

    public WeightStatistics(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        boolean isFilled = weight_instance != null && weight != null && measuringDate != null;
        if (!isFilled) {
            validationErrors.add("Invalid weight statistics : null value");
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weight_instance == null && weight == null;
    }

}
