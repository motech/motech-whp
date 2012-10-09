package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SmearTestRecordTest {

    @Test
    public void verifyDefaultSmearTestRecordInstance() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, null, null, null, null, null);

        assertFalse(preTreatmentRecord.isOfInstance(SampleInstance.EndTreatment));
        assertFalse(preTreatmentRecord.isOfInstance(SampleInstance.ExtendedIP));
        assertTrue(preTreatmentRecord.isOfInstance(SampleInstance.PreTreatment));
    }

    @Test
    public void shouldGiveTheCumulativeSmearTestResult(){
        SmearTestRecord positivePositiveTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Positive, null, SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord positiveNegativeTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Positive, null, SmearTestResult.Negative, "labName", "labNumber");
        SmearTestRecord positiveIndeterminateTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Positive, null, SmearTestResult.Indeterminate, "labName", "labNumber");
        SmearTestRecord negativePositiveTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Negative, null, SmearTestResult.Positive, "labName", "labNumber");

        SmearTestRecord negativeNegativeTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Negative, null, SmearTestResult.Negative, "labName", "labNumber");
        SmearTestRecord negativeIndeterminateTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Negative, null, SmearTestResult.Indeterminate, "labName", "labNumber");

        SmearTestRecord indeterminateIndeterminateTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Indeterminate, null, SmearTestResult.Indeterminate, "labName", "labNumber");

        assertThat(positivePositiveTreatmentRecord.cumulativeResult(), is(SmearTestResult.Positive));
        assertThat(positiveNegativeTreatmentRecord.cumulativeResult(), is(SmearTestResult.Positive));
        assertThat(positiveIndeterminateTreatmentRecord.cumulativeResult(), is(SmearTestResult.Positive));
        assertThat(negativePositiveTreatmentRecord.cumulativeResult(), is(SmearTestResult.Positive));

        assertThat(negativeNegativeTreatmentRecord.cumulativeResult(), is(SmearTestResult.Negative));
        assertThat(negativeIndeterminateTreatmentRecord.cumulativeResult(), is(SmearTestResult.Indeterminate));

        assertThat(indeterminateIndeterminateTreatmentRecord.cumulativeResult(), is(SmearTestResult.Indeterminate));
    }

    @Test
    public void shouldCheckIfSmearTestRecordIsPreTreatment(){
        SmearTestRecord preTreatmentSmearTestRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, SmearTestResult.Positive, null, SmearTestResult.Positive, "labName", "labNumber");
        SmearTestRecord eipSmearTestRecord = new SmearTestRecord(SampleInstance.ExtendedIP, null, SmearTestResult.Positive, null, SmearTestResult.Positive, "labName", "labNumber");

        assertTrue(preTreatmentSmearTestRecord.isPreTreatmentRecord());
        assertFalse(eipSmearTestRecord.isPreTreatmentRecord());
    }
}
