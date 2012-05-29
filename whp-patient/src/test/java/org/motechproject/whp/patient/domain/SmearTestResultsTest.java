package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmearTestResultsTest {

    @Test
    public void checkResultForSmearTestInstance() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.PreTreatment, null, null, null, null);

        assertFalse(preTreatmentRecord.isOfInstance(SmearTestSampleInstance.EndTreatment));
        assertFalse(preTreatmentRecord.isOfInstance(SmearTestSampleInstance.ExtendedIP));
        assertTrue(preTreatmentRecord.isOfInstance(SmearTestSampleInstance.PreTreatment));
    }
}
