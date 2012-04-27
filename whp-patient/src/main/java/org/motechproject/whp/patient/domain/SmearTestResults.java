package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.repository.ValidationErrors;

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

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        boolean isFilled = sampleInstance != null && testDate1 != null && result1 != null && testDate2 != null && result2 != null;
        if (!isFilled) {
            validationErrors.add("Invalid smear test results : null value");
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sampleInstance == null && testDate1 == null && result1 == null && testDate2 == null && result2 == null;
    }

}
