package org.motechproject.whp.migration.v0.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightStatisticsV0 {

    @JsonProperty
    private List<WeightStatisticsRecordV0> weightStatisticsRecords = new ArrayList<WeightStatisticsRecordV0>();

    public boolean add(WeightStatisticsRecordV0 weightStatisticsRecord) {
        if (weightStatisticsRecord.getMeasuringDate() != null) {
            WeightStatisticsRecordV0 existingResult = resultForInstance(weightStatisticsRecord.getWeight_instance());
            if (existingResult != null) {
                weightStatisticsRecords.remove(existingResult);
            }
            return weightStatisticsRecords.add(weightStatisticsRecord);
        }
        return false;
    }

    private WeightStatisticsRecordV0 resultForInstance(WeightInstanceV0 weightInstance) {
        for (WeightStatisticsRecordV0 weightStatisticsRecord : weightStatisticsRecords) {
            if (weightStatisticsRecord.isOfInstance(weightInstance))
                return weightStatisticsRecord;
        }
        return null;
    }

    public WeightStatisticsRecordV0 latestResult() {
        return weightStatisticsRecords.get(weightStatisticsRecords.size() - 1);
    }

    public WeightStatisticsV0 add(WeightInstanceV0 weightInstance, Double weight, LocalDate measuringDate) {
        if (weightInstance != null) {
            this.add(new WeightStatisticsRecordV0(weightInstance, weight, measuringDate));
        }
        return this;
    }

    public WeightStatisticsRecordV0 get(int index) {
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

        WeightStatisticsV0 that = (WeightStatisticsV0) o;

        if (weightStatisticsRecords != null ? !weightStatisticsRecords.equals(that.weightStatisticsRecords) : that.weightStatisticsRecords != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return weightStatisticsRecords != null ? weightStatisticsRecords.hashCode() : 0;
    }

    @JsonIgnore
    public List<WeightStatisticsRecordV0> getAll() {
        return Collections.unmodifiableList(weightStatisticsRecords);
    }
}