package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import java.util.List;

@Data
public class SmearTestRecord {

    private SampleInstance smear_sample_instance;
    private LocalDate smear_test_date_1;
    private SmearTestResult smear_test_result_1;
    private LocalDate smear_test_date_2;
    private SmearTestResult smear_test_result_2;

    public SmearTestRecord() {
    }

    public SmearTestRecord(SampleInstance sampleInstance, LocalDate testDate1, SmearTestResult result1, LocalDate testDate2, SmearTestResult result2) {
        this.smear_sample_instance = sampleInstance;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> validationErrors) {
        boolean isFilled = smear_sample_instance != null && smear_test_date_1 != null && smear_test_result_1 != null && smear_test_date_2 != null && smear_test_result_2 != null;
        if (!isFilled) {
            validationErrors.add(WHPErrorCode.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return smear_sample_instance == null && smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SampleInstance sampleInstance) {
        return this.smear_sample_instance.equals(sampleInstance);
    }

    public SmearTestResult cumulativeResult() {
        if(smear_test_result_1 == SmearTestResult.Positive || smear_test_result_2 == SmearTestResult.Positive)
            return SmearTestResult.Positive;
        if(smear_test_result_1 == SmearTestResult.Negative || smear_test_result_2 == SmearTestResult.Negative)
            return SmearTestResult.Negative;
        return SmearTestResult.Indeterminate;
    }

    @JsonIgnore
    public boolean isPreTreatmentRecord() {
        return smear_sample_instance == SampleInstance.PreTreatment;
    }
}
