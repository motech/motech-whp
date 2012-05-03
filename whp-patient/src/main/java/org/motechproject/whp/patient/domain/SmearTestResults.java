package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.repository.ValidationErrors;

@Data
public class SmearTestResults {

    private SmearTestSampleInstance smear_sample_instance;

    private LocalDate smear_test_date_1;
    private SmearTestResult smear_test_result_1;

    private LocalDate smear_test_date_2;
    private SmearTestResult smear_test_result_2;

    public SmearTestResults() {
    }

    public SmearTestResults(SmearTestSampleInstance sampleInstance1, LocalDate testDate1, SmearTestResult result1, LocalDate testDate2, SmearTestResult result2) {
        this.smear_sample_instance = sampleInstance1;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        boolean isFilled = smear_sample_instance != null && smear_test_date_1 != null && smear_test_result_1 != null && smear_test_date_2 != null && smear_test_result_2 != null;
        if (!isFilled) {
            validationErrors.add("Invalid smear test results : null value");
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return smear_sample_instance == null && smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

}
