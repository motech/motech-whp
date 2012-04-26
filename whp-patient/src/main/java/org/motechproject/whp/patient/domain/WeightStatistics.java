package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class WeightStatistics {

    private WeightInstance weightInstance;
    private double weight;
    private LocalDate measuringDate;

    public WeightStatistics() {
    }

    public WeightStatistics(WeightInstance weightInstance, double weight, LocalDate measuringDate) {
        this.weightInstance = weightInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }
}
