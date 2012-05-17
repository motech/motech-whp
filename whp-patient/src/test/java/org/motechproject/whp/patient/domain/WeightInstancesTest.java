package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.WeightInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class WeightInstancesTest {

    @Test
    public void shouldAddANewWeightResult_ForPreTreatment() {
        WeightStatistics preTreatmentResults = new WeightStatistics(WeightInstance.PreTreatment, null, null);

        WeightInstances weightInstances = new WeightInstances();
        weightInstances.add(preTreatmentResults);

        assertEquals(1, weightInstances.size());
        assertTrue(weightInstances.get(0).isOfInstance(WeightInstance.PreTreatment));
    }

    @Test
    public void shouldAddWeightResults_ForBothPreTreatment_AndEndTreatment() {
        WeightStatistics preTreatmentResults = new WeightStatistics(WeightInstance.PreTreatment, null, null);
        WeightStatistics endTreatmentResults = new WeightStatistics(WeightInstance.EndTreatment, null, null);

        WeightInstances weightInstances = new WeightInstances();
        weightInstances.add(preTreatmentResults);
        weightInstances.add(endTreatmentResults);

        assertEquals(2, weightInstances.size());
        assertTrue(weightInstances.get(0).isOfInstance(WeightInstance.PreTreatment));
        assertTrue(weightInstances.get(1).isOfInstance(WeightInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentWeightResult_ForPreTreatment() {
        WeightStatistics oldPreTreatmentResults = new WeightStatistics(WeightInstance.PreTreatment, null, new LocalDate(2010, 10, 10));
        WeightStatistics newPreTreatmentResults = new WeightStatistics(WeightInstance.PreTreatment, null, new LocalDate(2012, 12, 12));

        WeightInstances weightInstances = new WeightInstances();

        weightInstances.add(oldPreTreatmentResults);
        assertEquals(1, weightInstances.size());
        assertEquals(oldPreTreatmentResults, weightInstances.get(0));

        weightInstances.add(newPreTreatmentResults);
        assertEquals(1, weightInstances.size());
        assertEquals(newPreTreatmentResults, weightInstances.get(0));
    }
}
