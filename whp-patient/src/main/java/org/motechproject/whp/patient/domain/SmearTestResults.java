package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import java.util.ArrayList;
import java.util.List;

@Data
public class SmearTestResults {

    private SmearTestSampleInstance smear_sample_instance;

    private LocalDate smear_test_date_1;
    private SmearTestResult smear_test_result_1;

    private LocalDate smear_test_date_2;
    private SmearTestResult smear_test_result_2;

    public SmearTestResults() {
    }

    public SmearTestResults(SmearTestSampleInstance smearTestSampleInstance, LocalDate testDate1, SmearTestResult result1, LocalDate testDate2, SmearTestResult result2) {
        this.smear_sample_instance = smearTestSampleInstance;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
    }

    @JsonIgnore
    public boolean isValid(List<WHPDomainErrorCode> validationErrors) {
        boolean isFilled = smear_sample_instance != null && smear_test_date_1 != null && smear_test_result_1 != null && smear_test_date_2 != null && smear_test_result_2 != null;
        if (!isFilled) {
            validationErrors.add(WHPDomainErrorCode.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return smear_sample_instance == null && smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SmearTestSampleInstance smearTestSampleInstance) {
        return this.smear_sample_instance.equals(smearTestSampleInstance);
    }
}
