package org.motechproject.whp.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class WeightStatistics {

    private WeightInstance weightInstance;
    private float weight;
    private LocalDate measuringDate;

    public WeightStatistics() {
    }

    public WeightStatistics(WeightInstance weightInstance, float weight, LocalDate measuringDate) {
        this.weightInstance = weightInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }
}
