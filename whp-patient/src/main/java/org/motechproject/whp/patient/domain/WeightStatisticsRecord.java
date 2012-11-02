package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.io.Serializable;
import java.util.List;

@Data
public class WeightStatisticsRecord implements Serializable {

    private SputumTrackingInstance weight_instance;
    private Double weight;
    private LocalDate measuringDate;

    //Required for Ektorp
    public WeightStatisticsRecord() {
    }

    public WeightStatisticsRecord(SputumTrackingInstance SputumTrackingInstance, Double weight, LocalDate measuringDate) {
        this.weight_instance = SputumTrackingInstance;
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
    public boolean isOfInstance(SputumTrackingInstance SputumTrackingInstance) {
        return this.weight_instance.equals(SputumTrackingInstance);
    }
}
