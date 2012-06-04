package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmearTestResults {

    @JsonProperty
    private List<SmearTestRecord> all = new ArrayList<SmearTestRecord>();

    public boolean add(SmearTestRecord smearTestRecord) {
        if (smearTestRecord.getSmear_sample_instance() != null) {
            SmearTestRecord existingResult = resultForInstance(smearTestRecord.getSmear_sample_instance());
            if (existingResult != null) {
                this.all.remove(existingResult);
            }
            return this.all.add(smearTestRecord);
        }
        return false;
    }

    private SmearTestRecord resultForInstance(SmearTestSampleInstance smearTestSampleInstance) {
        for (SmearTestRecord smearTestRecord : this.all) {
            if (smearTestRecord.isOfInstance(smearTestSampleInstance))
                return smearTestRecord;
        }
        return null;
    }

    public SmearTestRecord latestResult() {
        return this.all.get(this.all.size() - 1);
    }

    public SmearTestRecord get(int index) {
        return this.all.get(index);
    }

    public int size() {
        return this.all.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.all.isEmpty();
    }

    public void add(SmearTestSampleInstance smearSampleInstance, LocalDate test1Date, SmearTestResult test1Result, LocalDate test2Date, SmearTestResult test2Result) {
        if (smearSampleInstance != null) {
            this.add(new SmearTestRecord(smearSampleInstance, test1Date, test1Result, test2Date, test2Result));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmearTestResults that = (SmearTestResults) o;

        if (all != null ? !all.equals(that.all) : that.all != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return all != null ? all.hashCode() : 0;
    }

    @JsonIgnore
    public List<SmearTestRecord> getAll() {
        return Collections.unmodifiableList(all);
    }
}
