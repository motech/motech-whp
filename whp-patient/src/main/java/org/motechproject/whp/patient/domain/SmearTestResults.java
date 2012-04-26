package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class SmearTestResults {

    private SmearTestSampleInstance sampleInstance;

    private LocalDate testDate1;
    private SmearTestResult result1;

    private LocalDate testDate2;
    private SmearTestResult result2;

    public SmearTestResults() {
    }

    public SmearTestResults(SmearTestSampleInstance sampleInstance1, LocalDate testDate1, SmearTestResult result1, LocalDate testDate2, SmearTestResult result2) {
        this.sampleInstance = sampleInstance1;
        this.testDate1 = testDate1;
        this.result1 = result1;
        this.testDate2 = testDate2;
        this.result2 = result2;
    }

}
