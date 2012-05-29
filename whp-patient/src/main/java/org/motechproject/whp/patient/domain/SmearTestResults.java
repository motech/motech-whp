package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import java.util.ArrayList;
import java.util.List;

public class SmearTestResults {

    @JsonProperty
    private List<SmearTestRecord> smearTestResults = new ArrayList<SmearTestRecord>();

    public boolean add(SmearTestRecord smearTestRecord) {
        if (smearTestRecord.getSmear_sample_instance() != null) {
            SmearTestRecord existingResult = resultForInstance(smearTestRecord.getSmear_sample_instance());
            if (existingResult != null) {
                this.smearTestResults.remove(existingResult);
            }
            return this.smearTestResults.add(smearTestRecord);
        }
        return false;
    }

    private SmearTestRecord resultForInstance(SmearTestSampleInstance smearTestSampleInstance) {
        for (SmearTestRecord smearTestRecord : this.smearTestResults) {
            if (smearTestRecord.isOfInstance(smearTestSampleInstance))
                return smearTestRecord;
        }
        return null;
    }

    public SmearTestRecord latestResult() {
        return this.smearTestResults.get(this.smearTestResults.size() - 1);
    }

    public SmearTestRecord get(int index) {
        return this.smearTestResults.get(index);
    }

    public int size() {
        return this.smearTestResults.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.smearTestResults.isEmpty();
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

        if (smearTestResults != null ? !smearTestResults.equals(that.smearTestResults) : that.smearTestResults != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return smearTestResults != null ? smearTestResults.hashCode() : 0;
    }
}
