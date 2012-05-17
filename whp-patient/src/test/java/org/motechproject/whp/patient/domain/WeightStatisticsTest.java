package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.WeightInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeightStatisticsTest {

    @Test
    public void checkResultForWeightInstance() {
        WeightStatistics endTreatmentResult = new WeightStatistics(WeightInstance.EndTreatment, null, null);

        assertFalse(endTreatmentResult.isOfInstance(WeightInstance.PreTreatment));
        assertFalse(endTreatmentResult.isOfInstance(WeightInstance.ExtendedIP));
        assertTrue(endTreatmentResult.isOfInstance(WeightInstance.EndTreatment));
    }
}
