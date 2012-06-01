package org.motechproject.whp.patient.domain;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static junit.framework.Assert.assertEquals;

public class SmearTestResultsTest {

    @Test
    public void shouldAddANewSmearTestResult_AllInstancesInOrder_shouldPreserveOrder() {
        SmearTestResults smearTestResults = new SmearTestResults();
        for(SmearTestSampleInstance type : SmearTestSampleInstance.values()) {
            SmearTestRecord SmearTestRecord = new SmearTestRecord(type, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive);
            smearTestResults.add(SmearTestRecord);
        }

        assertEquals(5, smearTestResults.size());
        Assert.assertTrue(smearTestResults.get(0).isOfInstance(SmearTestSampleInstance.PreTreatment));
        Assert.assertTrue(smearTestResults.get(1).isOfInstance(SmearTestSampleInstance.EndIP));
        Assert.assertTrue(smearTestResults.get(2).isOfInstance(SmearTestSampleInstance.ExtendedIP));
        Assert.assertTrue(smearTestResults.get(3).isOfInstance(SmearTestSampleInstance.TwoMonthsIntoCP));
        Assert.assertTrue(smearTestResults.get(4).isOfInstance(SmearTestSampleInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentSmearTestResult_AllInstancesInOrder_shouldPreserveOrder() {
        SmearTestResults smearTestResults = new SmearTestResults();
        for(SmearTestSampleInstance type : SmearTestSampleInstance.values()) {
            SmearTestRecord oldSmearTestRecord = new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive);
            smearTestResults.add(oldSmearTestRecord);
        }

        LocalDate newTestDate = new LocalDate(2010, 12, 12);
        for(SmearTestSampleInstance type : SmearTestSampleInstance.values()) {
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
        smearTestResults.add(new SmearTestRecord(SmearTestSampleInstance.PreTreatment, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SmearTestSampleInstance.PreTreatment));
        smearTestResults.add(new SmearTestRecord(SmearTestSampleInstance.EndIP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(2, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SmearTestSampleInstance.EndIP));
        smearTestResults.add(new SmearTestRecord(SmearTestSampleInstance.ExtendedIP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(3, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SmearTestSampleInstance.ExtendedIP));
        smearTestResults.add(new SmearTestRecord(SmearTestSampleInstance.TwoMonthsIntoCP, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(4, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SmearTestSampleInstance.TwoMonthsIntoCP));
        smearTestResults.add(new SmearTestRecord(SmearTestSampleInstance.EndTreatment, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(5, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(SmearTestSampleInstance.EndTreatment));
    }

    @Test
    public void shouldReplaceSmearTestResultsAsLatest_WhenResultsAreRecentForSameInstance() {
        for(SmearTestSampleInstance type : SmearTestSampleInstance.values()) {
            verifyFor(type);
        }
    }

    private void verifyFor(SmearTestSampleInstance type) {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Positive, new LocalDate(2010, 10, 10), SmearTestResult.Positive));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
        smearTestResults.add(new SmearTestRecord(type, new LocalDate(2010, 10, 10), SmearTestResult.Negative, new LocalDate(2010, 10, 10), SmearTestResult.Negative));
        assertEquals(1, smearTestResults.size());
        Assert.assertTrue(smearTestResults.latestResult().isOfInstance(type));
    }
}
