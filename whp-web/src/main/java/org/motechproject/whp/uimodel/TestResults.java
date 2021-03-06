package org.motechproject.whp.uimodel;

import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.patient.domain.WeightStatistics;

import java.util.ArrayList;

public class TestResults extends ArrayList<TestResult> {

    public TestResults(SmearTestResults smearTestResults, WeightStatistics weightStatistics) {
        for (SputumTrackingInstance sampleInstance : SputumTrackingInstance.values()) {
            if (smearTestResults.resultForInstance(sampleInstance) != null || weightStatistics.resultForInstance(sampleInstance) != null) {
                add(new TestResult(sampleInstance, smearTestResults.resultForInstance(sampleInstance), weightStatistics.resultForInstance(sampleInstance)));
            }
        }
    }
}
