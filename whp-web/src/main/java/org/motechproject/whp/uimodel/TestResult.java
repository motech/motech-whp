package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.whp.common.domain.WHPDate;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.WeightStatisticsRecord;
import org.motechproject.whp.refdata.domain.SampleInstance;

@Data
public class TestResult {
    private String smearTestResult1;
    private String smearTestDate1;
    private String smearTestResult2;
    private String smearTestDate2;
    private String weight;
    private String sampleInstance;
    private final String EMPTY_STRING = "";
    public TestResult(SampleInstance sampleInstance, SmearTestRecord smearTestRecord, WeightStatisticsRecord weightStatisticsRecord) {
        this.sampleInstance = sampleInstance.value();
        if (smearTestRecord == null) {
            smearTestDate1 = smearTestResult1 = smearTestDate2 = smearTestResult2 = EMPTY_STRING;
        }
        else {
              smearTestDate1 = WHPDate.date(smearTestRecord.getSmear_test_date_1()).value();
              smearTestDate2 = WHPDate.date(smearTestRecord.getSmear_test_date_2()).value();
              smearTestResult1 = smearTestRecord.getSmear_test_result_1() == null ? EMPTY_STRING : smearTestRecord.getSmear_test_result_1().value();
              smearTestResult2 = smearTestRecord.getSmear_test_result_2() == null ? EMPTY_STRING : smearTestRecord.getSmear_test_result_2().value();
        }

        if (weightStatisticsRecord == null) {
            weight = EMPTY_STRING;
        }
        else {
            weight = weightStatisticsRecord.getWeight().toString();
        }
    }


}
