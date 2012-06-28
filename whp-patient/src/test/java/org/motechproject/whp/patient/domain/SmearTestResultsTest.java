package org.motechproject.whp.patient.domain;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import static junit.framework.Assert.assertEquals;

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
        SmearTestResults smearTestResults = new SmearTestResults();
        for (SampleInstance type : SampleInstance.values()) {
            SmearTestRecord oldSmearTestRecord = new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive);
            smearTestResults.add(oldSmearTestRecord);
        }

        LocalDate newTestDate = new LocalDate(2010, 12, 12);
        for (SampleInstance type : SampleInstance.values()) {
            SmearTestRecord newSmearTestRecord = new SmearTestRecord(type, newTestDate, SmearTestResult.Negative, newTestDate, SmearTestResult.Negative);
            smearTestResults.add(newSmearTestRecord);
        }

        assertEquals(5, smearTestResults.size());
        Assert.assertTrue(smearTestResults.get(0).getSmear_test_date_1().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(0).getSmear_test_date_2().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(0).getSmear_test_result_1().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(0).getSmear_test_result_2().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(1).getSmear_test_date_1().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(1).getSmear_test_date_2().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(1).getSmear_test_result_1().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(1).getSmear_test_result_2().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(2).getSmear_test_date_1().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(2).getSmear_test_date_2().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(2).getSmear_test_result_1().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(2).getSmear_test_result_2().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(3).getSmear_test_date_1().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(3).getSmear_test_date_2().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(3).getSmear_test_result_1().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(3).getSmear_test_result_2().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(4).getSmear_test_date_1().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(4).getSmear_test_date_2().equals(newTestDate));
        Assert.assertTrue(smearTestResults.get(4).getSmear_test_result_1().equals(SmearTestResult.Negative));
        Assert.assertTrue(smearTestResults.get(4).getSmear_test_result_2().equals(SmearTestResult.Negative));
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

    private void verifyFor(SampleInstance type) {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Negative, new LocalDate(2010, 10, 10), SmearTestResult.Negative));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
    }
}
