package org.motechproject.whp.patient.domain;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class SmearTestResultsTest {

    @Test
    public void shouldAddANewSmearTestResult_AllInstancesInOrder_shouldPreserveOrder() {
        SmearTestResults smearTestResults = new SmearTestResults();
        for (SampleInstance type : SampleInstance.values()) {
            SmearTestRecord SmearTestRecord = new SmearTestRecord(type, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive);
            smearTestResults.add(SmearTestRecord);
        }

        assertEquals(5, smearTestResults.size());
        Assert.assertTrue(smearTestResults.get(0).isOfInstance(SampleInstance.PreTreatment));
        Assert.assertTrue(smearTestResults.get(1).isOfInstance(SampleInstance.EndIP));
        Assert.assertTrue(smearTestResults.get(2).isOfInstance(SampleInstance.ExtendedIP));
        Assert.assertTrue(smearTestResults.get(3).isOfInstance(SampleInstance.TwoMonthsIntoCP));
        Assert.assertTrue(smearTestResults.get(4).isOfInstance(SampleInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentSmearTestResult_AllInstancesInOrder_shouldPreserveOrder() {

        SampleInstance toBeUpdatedInstance = SampleInstance.PreTreatment;
        SampleInstance anotherInstance = SampleInstance.EndIP;
        LocalDate anotherInstanceDate1 = new LocalDate(2011, 1, 2);
        LocalDate anotherInstanceDate2 = new LocalDate(2011, 1, 13);
        SmearTestResult anotherInstanceResult1 = SmearTestResult.Positive;
        SmearTestResult anotherInstanceResult2 = SmearTestResult.Negative;

        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord oldSmearTestRecord1 = new SmearTestRecord(toBeUpdatedInstance, new LocalDate(2010, 10, 10), SmearTestResult.Negative, new LocalDate(2010, 11, 15), SmearTestResult.Positive);
        SmearTestRecord oldSmearTestRecord2 = new SmearTestRecord(anotherInstance, anotherInstanceDate1, anotherInstanceResult1, anotherInstanceDate2, anotherInstanceResult2);
        smearTestResults.add(oldSmearTestRecord1);
        smearTestResults.add(oldSmearTestRecord2);

        LocalDate newTestDate1 = new LocalDate(2010, 12, 12);
        LocalDate newTestDate2 = new LocalDate(2010, 12, 20);
        SmearTestResult newResult1 = SmearTestResult.Negative;
        SmearTestResult newResult2 = SmearTestResult.Positive;
        SmearTestRecord newSmearTestRecord = new SmearTestRecord(toBeUpdatedInstance, newTestDate1, newResult1, newTestDate2, newResult2);
        smearTestResults.add(newSmearTestRecord);

        assertEquals(2, smearTestResults.size());
        assertSmearTestResult(smearTestResults.get(0), anotherInstance, anotherInstanceDate1, anotherInstanceResult1, anotherInstanceDate2, anotherInstanceResult2);
        assertSmearTestResult(smearTestResults.get(1), toBeUpdatedInstance, newTestDate1, newResult1, newTestDate2, newResult2);
    }

    @Test
    public void shouldAddSmearTestResultsAsLatest_WhenResultsAreForDifferentInstance() {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(SampleInstance.PreTreatment, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SampleInstance.PreTreatment));
        smearTestResults.add(new SmearTestRecord(SampleInstance.EndIP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(2, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SampleInstance.EndIP));
        smearTestResults.add(new SmearTestRecord(SampleInstance.ExtendedIP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(3, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SampleInstance.ExtendedIP));
        smearTestResults.add(new SmearTestRecord(SampleInstance.TwoMonthsIntoCP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(4, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SampleInstance.TwoMonthsIntoCP));
        smearTestResults.add(new SmearTestRecord(SampleInstance.EndTreatment, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(5, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SampleInstance.EndTreatment));
    }

    @Test
    public void shouldReplaceSmearTestResultsAsLatest_WhenResultsAreRecentForSameInstance() {
        for (SampleInstance type : SampleInstance.values()) {
            verifyFor(type);
        }
    }


    @Test
    public void shouldReturnSmearTestResultForGivenSampleInstance() {
        SmearTestResults smearTestResults = new SmearTestResults();
        SmearTestRecord pretreatmentSmearTestRecord = new SmearTestRecord(SampleInstance.PreTreatment, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive);
        SmearTestRecord endIpSmearTestRecord = new SmearTestRecord(SampleInstance.EndIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive);

        smearTestResults.add(endIpSmearTestRecord);
        smearTestResults.add(pretreatmentSmearTestRecord);

        assertThat(smearTestResults.resultForInstance(SampleInstance.PreTreatment), is(pretreatmentSmearTestRecord));
        assertThat(smearTestResults.resultForInstance(SampleInstance.EndIP), is(endIpSmearTestRecord));
        assertThat(smearTestResults.resultForInstance(SampleInstance.ExtendedIP), nullValue());
    }

    private void verifyFor(SampleInstance type) {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Negative, new LocalDate(2010, 10, 10), SmearTestResult.Negative));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
    }

    private void assertSmearTestResult(SmearTestRecord smearTestRecord, SampleInstance expectedInstance, LocalDate expectedDate1, SmearTestResult expectedResult1, LocalDate expectedDate2, SmearTestResult expectedResult2) {
        assertEquals(expectedInstance,smearTestRecord.getSmear_sample_instance());
        assertEquals(expectedDate1, smearTestRecord.getSmear_test_date_1());
        assertEquals(expectedDate2, smearTestRecord.getSmear_test_date_2());
        assertEquals(expectedResult1, smearTestRecord.getSmear_test_result_1());
        assertEquals(expectedResult2, smearTestRecord.getSmear_test_result_2());
    }
}
