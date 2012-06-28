package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SampleInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeightStatisticsRecordTest {

    @Test
    public void verifyDefaultWeightStatisticsInstance() {
        WeightStatisticsRecord endTreatmentResult = new WeightStatisticsRecord(SampleInstance.EndTreatment, null, null);
        assertFalse(endTreatmentResult.isOfInstance(SampleInstance.PreTreatment));
        assertFalse(endTreatmentResult.isOfInstance(SampleInstance.ExtendedIP));
        assertTrue(endTreatmentResult.isOfInstance(SampleInstance.EndTreatment));
    }
}
