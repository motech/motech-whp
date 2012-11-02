package org.motechproject.whp.migration.v0.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmearTestResultsV0 {

    @JsonProperty
    private List<SmearTestRecordV0> all = new ArrayList<>();

    public boolean add(SmearTestRecordV0 smearTestRecord) {
        if (smearTestRecord.getSmear_sample_instance() != null) {
            SmearTestRecordV0 existingResult = resultForInstance(smearTestRecord.getSmear_sample_instance());
            if (existingResult != null) {
                this.all.remove(existingResult);
            }
            return this.all.add(smearTestRecord);
        }
        return false;
    }

    private SmearTestRecordV0 resultForInstance(SmearTestSputumTrackingInstanceV0 smearTestSputumTrackingInstance) {
        for (SmearTestRecordV0 smearTestRecord : this.all) {
            if (smearTestRecord.isOfInstance(smearTestSputumTrackingInstance))
                return smearTestRecord;
        }
        return null;
    }

    public SmearTestRecordV0 latestResult() {
        return this.all.get(this.all.size() - 1);
    }

    public SmearTestRecordV0 get(int index) {
        return this.all.get(index);
    }

    public int size() {
        return this.all.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.all.isEmpty();
    }

    public void add(SmearTestSputumTrackingInstanceV0 smearSputumTrackingInstance, LocalDate test1Date, SmearTestResultV0 test1Result, LocalDate test2Date, SmearTestResultV0 test2Result) {
        if (smearSputumTrackingInstance != null) {
            this.add(new SmearTestRecordV0(smearSputumTrackingInstance, test1Date, test1Result, test2Date, test2Result));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmearTestResultsV0 that = (SmearTestResultsV0) o;

        if (all != null ? !all.equals(that.all) : that.all != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return all != null ? all.hashCode() : 0;
    }

    @JsonIgnore
    public List<SmearTestRecordV0> getAll() {
        return Collections.unmodifiableList(all);
    }
}