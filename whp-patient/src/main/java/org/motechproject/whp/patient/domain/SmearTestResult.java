package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class SmearTestResult {

    private String sampleInstance1;
    private LocalDate testDate1;
    private String result1;

    private String sampleInstance2;
    private LocalDate testDate2;
    private String result2;

    public SmearTestResult(String sampleInstance1, LocalDate testDate1, String result1, String sampleInstance2, LocalDate testDate2, String result2) {
        this.sampleInstance1 = sampleInstance1;
        this.testDate1 = testDate1;
        this.result1 = result1;
        this.sampleInstance2 = sampleInstance2;
        this.testDate2 = testDate2;
        this.result2 = result2;
    }

}
