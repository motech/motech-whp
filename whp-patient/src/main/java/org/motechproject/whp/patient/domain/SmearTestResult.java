package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

public class SmearTestResult {

    @JsonProperty
    private String sampleInstance1;
    @JsonProperty
    private LocalDate testDate1;
    @JsonProperty
    private String result1;

    @JsonProperty
    private String sampleInstance2;
    @JsonProperty
    private LocalDate testDate2;
    @JsonProperty
    private String result2;

    public SmearTestResult(String sampleInstance1, LocalDate testDate1, String result1, String sampleInstance2, LocalDate testDate2, String result2) {
        this.sampleInstance1 = sampleInstance1;
        this.testDate1 = testDate1;
        this.result1 = result1;
        this.sampleInstance2 = sampleInstance2;
        this.testDate2 = testDate2;
        this.result2 = result2;
    }

    public String getSampleInstance1() {
        return sampleInstance1;
    }

    public void setSampleInstance1(String sampleInstance1) {
        this.sampleInstance1 = sampleInstance1;
    }

    public LocalDate getTestDate1() {
        return testDate1;
    }

    public void setTestDate1(LocalDate testDate1) {
        this.testDate1 = testDate1;
    }

    public String getResult1() {
        return result1;
    }

    public void setResult1(String result1) {
        this.result1 = result1;
    }

    public String getSampleInstance2() {
        return sampleInstance2;
    }

    public void setSampleInstance2(String sampleInstance2) {
        this.sampleInstance2 = sampleInstance2;
    }

    public LocalDate getTestDate2() {
        return testDate2;
    }

    public void setTestDate2(LocalDate testDate2) {
        this.testDate2 = testDate2;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String result2) {
        this.result2 = result2;
    }

}
