package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;

public class SmearTestResultsTest {

    @Test
    public void shouldAddANewSmearTestResultPreservingOrder() {
        SmearTestResults smearTestResults = new SmearTestResults();

        SmearTestRecord SmearTestRecord1 = new SmearTestRecord(SputumTrackingInstance.EndIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord SmearTestRecord2 = new SmearTestRecord(SputumTrackingInstance.ExtendedIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");

        smearTestResults.add(SmearTestRecord1);
        smearTestResults.add(SmearTestRecord2);

        assertEquals(2, smearTestResults.size());
        assertThat(smearTestResults.get(0).getSmear_sample_instance(),is(SputumTrackingInstance.EndIP));
        assertThat(smearTestResults.get(1).getSmear_sample_instance(),is(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void shouldUpdateCurrentSmearTestResultAndPushToEndOfList() {

        SputumTrackingInstance toBeUpdatedInstance = SputumTrackingInstance.PreTreatment;
        SputumTrackingInstance anotherInstance = SputumTrackingInstance.EndIP;
        LocalDate anotherInstanceDate1 = new LocalDate(2011, 1, 2);
        LocalDate anotherInstanceDate2 = new LocalDate(2011, 1, 13);
        SmearTestResult anotherInstanceResult1 = SmearTestResult.Positive;
        SmearTestResult anotherInstanceResult2 = SmearTestResult.Negative;

        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord oldSmearTestRecord1 = new SmearTestRecord(toBeUpdatedInstance, new LocalDate(2010, 10, 10), SmearTestResult.Negative, new LocalDate(2010, 11, 15), SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord oldSmearTestRecord2 = new SmearTestRecord(anotherInstance, anotherInstanceDate1, anotherInstanceResult1, anotherInstanceDate2, anotherInstanceResult2, "labName", "labNumber");
        smearTestResults.add(oldSmearTestRecord1);
        smearTestResults.add(oldSmearTestRecord2);

        LocalDate newTestDate1 = new LocalDate(2010, 12, 12);
        LocalDate newTestDate2 = new LocalDate(2010, 12, 20);
        SmearTestResult newResult1 = SmearTestResult.Negative;
        SmearTestResult newResult2 = SmearTestResult.Positive;
        SmearTestRecord newSmearTestRecord = new SmearTestRecord(toBeUpdatedInstance, newTestDate1, newResult1, newTestDate2, newResult2, "labName", "labNumber");
        smearTestResults.add(newSmearTestRecord);

        assertEquals(2, smearTestResults.size());
        assertSmearTestResult(smearTestResults.get(0), anotherInstance, anotherInstanceDate1, anotherInstanceResult1, anotherInstanceDate2, anotherInstanceResult2);
        assertSmearTestResult(smearTestResults.get(1), toBeUpdatedInstance, newTestDate1, newResult1, newTestDate2, newResult2);
    }

    @Test
    public void shouldReturnLastResultInList() {
        SmearTestResults smearTestResults = new SmearTestResults();

        smearTestResults.add(new SmearTestRecord(SputumTrackingInstance.PreTreatment, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive, "labName", "labNumber"));
        assertThat(smearTestResults.latestResult().getSmear_sample_instance(), is(SputumTrackingInstance.PreTreatment));

        smearTestResults.add(new SmearTestRecord(SputumTrackingInstance.EndIP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive, "labName", "labNumber"));
        assertThat(smearTestResults.latestResult().getSmear_sample_instance(), is(SputumTrackingInstance.EndIP));
    }

    @Test
    public void shouldReturnSmearTestResultForGivenSputumTrackingInstance() {
        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord pretreatmentSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord endIpSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.EndIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");

        smearTestResults.add(endIpSmearTestRecord);
        smearTestResults.add(pretreatmentSmearTestRecord);

        assertThat(smearTestResults.resultForInstance(SputumTrackingInstance.PreTreatment), is(pretreatmentSmearTestRecord));
        assertThat(smearTestResults.resultForInstance(SputumTrackingInstance.EndIP), is(endIpSmearTestRecord));
        assertThat(smearTestResults.resultForInstance(SputumTrackingInstance.ExtendedIP), nullValue());
    }

    private void assertSmearTestResult(SmearTestRecord smearTestRecord, SputumTrackingInstance expectedInstance, LocalDate expectedDate1, SmearTestResult expectedResult1, LocalDate expectedDate2, SmearTestResult expectedResult2) {
        assertEquals(expectedInstance, smearTestRecord.getSmear_sample_instance());
        assertEquals(expectedDate1, smearTestRecord.getSmear_test_date_1());
        assertEquals(expectedDate2, smearTestRecord.getSmear_test_date_2());
        assertEquals(expectedResult1, smearTestRecord.getSmear_test_result_1());
        assertEquals(expectedResult2, smearTestRecord.getSmear_test_result_2());
    }

    @Test
    public void shouldGetPreTreatmentSmearTestResult(){
        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord pretreatmentSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord endIpSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.EndIP, DateUtil.today(), SmearTestResult.Negative, DateUtil.today(), SmearTestResult.Negative, "labName", "labNumber");
        smearTestResults.add(endIpSmearTestRecord);
        smearTestResults.add(pretreatmentSmearTestRecord);

        assertEquals(SmearTestResult.Positive, smearTestResults.getPreTreatmentResult());
    }

    @Test
    public void shouldReturnIfPreTreatmentSmearTestResultExists(){
        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord pretreatmentSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord endIpSmearTestRecord = new SmearTestRecord(SputumTrackingInstance.EndIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber");
        smearTestResults.add(endIpSmearTestRecord);
        smearTestResults.add(pretreatmentSmearTestRecord);

        assertTrue(smearTestResults.hasPreTreatmentResult());
    }
}
