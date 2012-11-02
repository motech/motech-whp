package org.motechproject.whp.container.domain;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SputumTrackingInstanceTest {
    @Test
    public void shouldGetInstanceForGivenValue() {
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-Treatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceForValue("Pre-treatment"));
    }

    @Test
    public void shouldGetInstanceForGivenName() {
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceByName("PreTreatment"));
        assertEquals(SputumTrackingInstance.PreTreatment, SputumTrackingInstance.getInstanceByName("Pretreatment"));
    }

    @Test
    public void shouldListAllInTreatmentInstances() {
        SputumTrackingInstance[] instances = SputumTrackingInstance.IN_TREATMENT_INSTANCES;
        assertEquals(7, instances.length);
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.EndIP));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.ExtendedIP));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.TwoMonthsIntoCP));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.EndTreatment));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.TreatmentInterruption1));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.TreatmentInterruption2));
        assertTrue(ArrayUtils.contains(instances, SputumTrackingInstance.TreatmentInterruption3));
    }
}
