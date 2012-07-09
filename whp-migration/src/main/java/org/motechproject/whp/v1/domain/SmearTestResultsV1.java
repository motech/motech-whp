package org.motechproject.whp.v1.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmearTestResultsV1 {

    @JsonProperty
    private List<SmearTestRecordV1> all = new ArrayList<SmearTestRecordV1>();

    public boolean add(SmearTestRecordV1 smearTestRecord) {
        if (smearTestRecord.getSmear_sample_instance() != null) {
            SmearTestRecordV1 existingResult = resultForInstance(smearTestRecord.getSmear_sample_instance());
            if (existingResult != null) {
                this.all.remove(existingResult);
            }
            return this.all.add(smearTestRecord);
        }
        return false;
    }

    public SmearTestRecordV1 resultForInstance(SampleInstanceV1 sampleInstance) {
        for (SmearTestRecordV1 smearTestRecord : this.all) {
            if (smearTestRecord.isOfInstance(sampleInstance))
                return smearTestRecord;
        }
        return null;
    }

    public SmearTestRecordV1 latestResult() {
        return this.all.get(this.all.size() - 1);
    }

    public SmearTestRecordV1 get(int index) {
        return this.all.get(index);
    }

    public int size() {
        return this.all.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.all.isEmpty();
    }

    public void add(SampleInstanceV1 smearSampleInstance, LocalDate test1Date, SmearTestResult test1Result, LocalDate test2Date, SmearTestResult test2Result) {
        if (smearSampleInstance != null) {
            this.add(new SmearTestRecordV1(smearSampleInstance, test1Date, test1Result, test2Date, test2Result));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmearTestResultsV1 that = (SmearTestResultsV1) o;

        if (all != null ? !all.equals(that.all) : that.all != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return all != null ? all.hashCode() : 0;
    }

    @JsonIgnore
    public List<SmearTestRecordV1> getAll() {
        return Collections.unmodifiableList(all);
    }
}
