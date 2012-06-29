package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.WeightStatisticsRecord;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestResultTest {

    SampleInstance sampleInstance = SampleInstance.EndIP;
    String date1;
    String result1;
    String date2;
    String result2;
    String weight;
    SmearTestRecord smearTestRecord;
    WeightStatisticsRecord weightStatisticsRecord;

    @Before
    public void setup() {
        LocalDate date1 = new LocalDate(2012, 6, 28);
        this.date1 = "28/06/2012";
        SmearTestResult result1 = SmearTestResult.Positive;
        this.result1 = result1.value();
        LocalDate date2 = new LocalDate(2012, 6, 29);
        this.date2 = "29/06/2012";
        SmearTestResult result2 = SmearTestResult.Negative;
        this.result2 = result2.value();
        smearTestRecord = new SmearTestRecord(sampleInstance, date1, result1, date2, result2);
        weight = "20.0";
        weightStatisticsRecord = new WeightStatisticsRecord(sampleInstance, Double.valueOf(weight), new LocalDate(2012, 1, 1));
    }

    @Test
    public void shouldCreateTestResult() {
        TestResult testResult = new TestResult(sampleInstance, smearTestRecord, weightStatisticsRecord);

        assertTestResults(testResult, sampleInstance, "28/06/2012", "29/06/2012", result1, result2, String.valueOf(weight));
    }

    @Test
    public void shouldSetEmptyStringIfTestInstanceIsNull() {
        TestResult testResult = new TestResult(sampleInstance, null, weightStatisticsRecord);

        assertTestResults(testResult, sampleInstance, "", "", "", "", String.valueOf(weight));

        testResult = new TestResult(sampleInstance, smearTestRecord, null);

        assertTestResults(testResult, sampleInstance, "28/06/2012", "29/06/2012", result1, result2, "");
    }

    @Test
    public void shouldSetEmptyStringWhenTestResultValueFieldsAreNull() {
        SmearTestRecord smearTestRecord = new SmearTestRecord(sampleInstance, null, null, null, null);

        TestResult testResult = new TestResult(sampleInstance, smearTestRecord, weightStatisticsRecord);

        assertTestResults(testResult, sampleInstance, "", "", "", "", String.valueOf(weight));
    }

    private void assertTestResults(TestResult testResult, SampleInstance sampleInstance, String date1, String date2, String result1, String result2, String weight) {
        assertThat(testResult.getSampleInstance(), is(sampleInstance.value()));
        assertThat(testResult.getSmearTestDate1(), is(date1));
        assertThat(testResult.getSmearTestDate2(), is(date2));
        assertThat(testResult.getSmearTestResult1(), is(result1));
        assertThat(testResult.getSmearTestResult2(), is(result2));
        assertThat(testResult.getWeight(), is(weight));
    }
}