package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

@Data
public class SmearTestRecordV0 {

    private SmearTestSampleInstanceV0 smear_sample_instance;
    private LocalDate smear_test_date_1;
    private SmearTestResultV0 smear_test_result_1;
    private LocalDate smear_test_date_2;
    private SmearTestResultV0 smear_test_result_2;

    public SmearTestRecordV0() {
    }

    public SmearTestRecordV0(SmearTestSampleInstanceV0 smearTestSampleInstance, LocalDate testDate1, SmearTestResultV0 result1, LocalDate testDate2, SmearTestResultV0 result2) {
        this.smear_sample_instance = smearTestSampleInstance;
        this.smear_test_date_1 = testDate1;
        this.smear_test_result_1 = result1;
        this.smear_test_date_2 = testDate2;
        this.smear_test_result_2 = result2;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return smear_sample_instance == null && smear_test_date_1 == null && smear_test_result_1 == null && smear_test_date_2 == null && smear_test_result_2 == null;
    }

    @JsonIgnore
    public boolean isOfInstance(SmearTestSampleInstanceV0 smearTestSampleInstance) {
        return this.smear_sample_instance.equals(smearTestSampleInstance);
    }
}