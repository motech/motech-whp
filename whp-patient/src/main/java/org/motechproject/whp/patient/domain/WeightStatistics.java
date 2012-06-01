package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.WeightInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightStatistics {

    @JsonProperty
    private List<WeightStatisticsRecord> weightStatisticsRecords = new ArrayList<WeightStatisticsRecord>();

    public boolean add(WeightStatisticsRecord weightStatisticsRecord) {
        if (weightStatisticsRecord.getMeasuringDate() != null) {
            WeightStatisticsRecord existingResult = resultForInstance(weightStatisticsRecord.getWeight_instance());
            if (existingResult != null) {
                weightStatisticsRecords.remove(existingResult);
            }
            return weightStatisticsRecords.add(weightStatisticsRecord);
        }
        return false;
    }

    private WeightStatisticsRecord resultForInstance(WeightInstance weightInstance) {
        for (WeightStatisticsRecord weightStatisticsRecord : weightStatisticsRecords) {
            if (weightStatisticsRecord.isOfInstance(weightInstance))
                return weightStatisticsRecord;
        }
        return null;
    }

    public WeightStatisticsRecord latestResult() {
        return weightStatisticsRecords.get(weightStatisticsRecords.size() - 1);
    }

    public WeightStatistics add(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        if (weightInstance != null) {
            this.add(new WeightStatisticsRecord(weightInstance, weight, measuringDate));
        }
        return this;
    }

    public WeightStatisticsRecord get(int index) {
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

        WeightStatistics that = (WeightStatistics) o;

        if (weightStatisticsRecords != null ? !weightStatisticsRecords.equals(that.weightStatisticsRecords) : that.weightStatisticsRecords != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return weightStatisticsRecords != null ? weightStatisticsRecords.hashCode() : 0;
    }

    @JsonIgnore
    public List<WeightStatisticsRecord> getAll() {
        return Collections.unmodifiableList(weightStatisticsRecords);
    }
}
