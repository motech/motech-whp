package org.motechproject.whp.v1.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightStatisticsV1 {

    @JsonProperty
    private List<WeightStatisticsRecordV1> weightStatisticsRecords = new ArrayList<WeightStatisticsRecordV1>();

    public boolean add(WeightStatisticsRecordV1 weightStatisticsRecord) {
        if (weightStatisticsRecord.getMeasuringDate() != null) {
            WeightStatisticsRecordV1 existingResult = resultForInstance(weightStatisticsRecord.getWeight_instance());
            if (existingResult != null) {
                weightStatisticsRecords.remove(existingResult);
            }
            return weightStatisticsRecords.add(weightStatisticsRecord);
        }
        return false;
    }

    public WeightStatisticsRecordV1 resultForInstance(SampleInstanceV1 SampleInstance) {
        for (WeightStatisticsRecordV1 weightStatisticsRecord : weightStatisticsRecords) {
            if (weightStatisticsRecord.isOfInstance(SampleInstance))
                return weightStatisticsRecord;
        }
        return null;
    }

    public WeightStatisticsRecordV1 latestResult() {
        return weightStatisticsRecords.get(weightStatisticsRecords.size() - 1);
    }

    public WeightStatisticsV1 add(SampleInstanceV1 SampleInstance, Double weight, LocalDate measuringDate) {
        if (SampleInstance != null) {
            this.add(new WeightStatisticsRecordV1(SampleInstance, weight, measuringDate));
        }
        return this;
    }

    public WeightStatisticsRecordV1 get(int index) {
        return weightStatisticsRecords.get(index);
    }

    public int size() {
        return weightStatisticsRecords.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return weightStatisticsRecords.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightStatisticsV1 that = (WeightStatisticsV1) o;

        if (weightStatisticsRecords != null ? !weightStatisticsRecords.equals(that.weightStatisticsRecords) : that.weightStatisticsRecords != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return weightStatisticsRecords != null ? weightStatisticsRecords.hashCode() : 0;
    }

    @JsonIgnore
    public List<WeightStatisticsRecordV1> getAll() {
        return Collections.unmodifiableList(weightStatisticsRecords);
    }
}
