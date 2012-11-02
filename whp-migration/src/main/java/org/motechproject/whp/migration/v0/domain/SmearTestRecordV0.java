package org.motechproject.whp.migration.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.migration.v0.exception.WHPErrorCodeV0;

import java.util.List;

@Data
public class SmearTestRecordV0 {

    private SmearTestSputumTrackingInstanceV0 smear_sample_instance;
    private LocalDate smear_test_date_1;
    private SmearTestResultV0 smear_test_result_1;
    private LocalDate smear_test_date_2;
    private SmearTestResultV0 smear_test_result_2;

    public SmearTestRecordV0() {
    }

    public SmearTestRecordV0(SmearTestSputumTrackingInstanceV0 smearTestSputumTrackingInstance, LocalDate testDate1, SmearTestResultV0 result1, LocalDate testDate2, SmearTestResultV0 result2) {
        this.smear_sample_instance = smearTestSputumTrackingInstance;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCodeV0> validationErrors) {
        boolean isFilled = smear_sample_instance != null && smear_test_date_1 != null && smear_test_result_1 != null && smear_test_date_2 != null && smear_test_result_2 != null;
        if (!isFilled) {
            validationErrors.add(WHPErrorCodeV0.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return smear_sample_instance == null && smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SmearTestSputumTrackingInstanceV0 smearTestSputumTrackingInstance) {
        return this.smear_sample_instance.equals(smearTestSputumTrackingInstance);
    }
}