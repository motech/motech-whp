package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SampleInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmearTestRecordTest {

    @Test
    public void verifyDefaultSmearTestRecordInstance() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SampleInstance.PreTreatment, null, null, null, null);

        assertFalse(preTreatmentRecord.isOfInstance(SampleInstance.EndTreatment));
        assertFalse(preTreatmentRecord.isOfInstance(SampleInstance.ExtendedIP));
        assertTrue(preTreatmentRecord.isOfInstance(SampleInstance.PreTreatment));
    }
}
