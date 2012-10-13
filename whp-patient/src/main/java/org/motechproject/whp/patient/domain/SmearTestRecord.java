package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.io.Serializable;
import java.util.List;

@Data
public class SmearTestRecord implements Serializable {

    private SampleInstance smear_sample_instance;
    private LocalDate smear_test_date_1;
    private SmearTestResult smear_test_result_1;
    private LocalDate smear_test_date_2;
    private SmearTestResult smear_test_result_2;
    private String labName;
    private String labNumber;

    public SmearTestRecord() {
    }

    public SmearTestRecord(SampleInstance sampleInstance, LocalDate testDate1, SmearTestResult result1, LocalDate testDate2, SmearTestResult result2, String labName, String labNumber) {
        this.smear_sample_instance = sampleInstance;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
        this.labName = labName;
        this.labNumber = labNumber;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> validationErrors) {
        boolean isFilled = smear_sample_instance != null && (allResultsNonNull() || allResultsNull());
        if (!isFilled) {
            validationErrors.add(WHPErrorCode.SPUTUM_LAB_RESULT_IS_INCOMPLETE);
        }
        return isFilled;
    }

    private boolean allResultsNull() {
        return smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

    private boolean allResultsNonNull() {
        return smear_test_date_1 != null && smear_test_result_1 != null && smear_test_date_2 != null && smear_test_result_2 != null;
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
        return smear_test_result_1.cumulativeResult(smear_test_result_2);
    }

    @JsonIgnore
    public boolean isPreTreatmentRecord() {
        return smear_sample_instance == SampleInstance.PreTreatment;
    }
}
