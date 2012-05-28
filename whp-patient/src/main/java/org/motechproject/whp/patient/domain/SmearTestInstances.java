package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import java.util.ArrayList;
import java.util.List;

public class SmearTestInstances {

    @JsonProperty
    private List<SmearTestResults> smearTestResults = new ArrayList<SmearTestResults>();

    public boolean add(SmearTestResults smearTestResults) {
        return this.smearTestResults.add(smearTestResults);
    }

    public SmearTestResults latestResult() {
        return this.smearTestResults.get(this.smearTestResults.size() - 1);
    }

    public SmearTestResults get(int index) {
        return this.smearTestResults.get(index);
    }

    public int size() {
        return this.smearTestResults.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.smearTestResults.isEmpty();
    }
}
