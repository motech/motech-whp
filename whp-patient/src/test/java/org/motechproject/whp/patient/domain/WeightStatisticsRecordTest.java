package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeightStatisticsRecordTest {

    @Test
    public void verifyDefaultWeightStatisticsInstance() {
        WeightStatisticsRecord endTreatmentResult = new WeightStatisticsRecord(SputumTrackingInstance.EndTreatment, null, null);
        assertFalse(endTreatmentResult.isOfInstance(SputumTrackingInstance.PreTreatment));
        assertFalse(endTreatmentResult.isOfInstance(SputumTrackingInstance.ExtendedIP));
        assertTrue(endTreatmentResult.isOfInstance(SputumTrackingInstance.EndTreatment));
    }

    @Test
    public void shouldReturnWhetherWeightStatisticsInstanceIsEmptyOrNot() {
        WeightStatisticsRecord emptyTreatmentResult = new WeightStatisticsRecord(SputumTrackingInstance.EndTreatment, null, null);
        WeightStatisticsRecord nonEmptyTreatmentResult = new WeightStatisticsRecord(SputumTrackingInstance.EndTreatment, new Double(80), null);
        assertTrue(emptyTreatmentResult.isEmpty());
        assertFalse(nonEmptyTreatmentResult.isEmpty());
    }
}
