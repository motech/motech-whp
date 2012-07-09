package org.motechproject.whp.domain.v1;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

@Data
public class WeightStatisticsRecordV1 {

    private SampleInstanceV1 weight_instance;
    private Double weight;
    private LocalDate measuringDate;

    //Required for Ektorp
    public WeightStatisticsRecordV1() {
    }

    public WeightStatisticsRecordV1(SampleInstanceV1 SampleInstance, Double weight, LocalDate measuringDate) {
        this.weight_instance = SampleInstance;
        this.weight = weight;
        this.measuringDate = measuringDate;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weight_instance == null && weight == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SampleInstanceV1 SampleInstance) {
        return this.weight_instance.equals(SampleInstance);
    }
}
