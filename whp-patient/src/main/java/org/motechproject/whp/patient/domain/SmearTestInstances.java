package org.motechproject.whp.patient.domain;

import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import java.util.ArrayList;

public class SmearTestInstances extends ArrayList<SmearTestResults> {

    @Override
    public boolean add(SmearTestResults smearTestResults) {
        SmearTestResults existingResult = resultForInstance(smearTestResults.getSmear_sample_instance());
        if (existingResult != null) {
            remove(existingResult);
        }
        return super.add(smearTestResults);
    }

    private SmearTestResults resultForInstance(SmearTestSampleInstance smearTestSampleInstance) {
        for (SmearTestResults smearTestResults : this) {
            if (smearTestResults.isOfInstance(smearTestSampleInstance))
                return smearTestResults;
        }
        return null;
    }

    public SmearTestResults latestResult() {
        return get(size() - 1);
    }
}
