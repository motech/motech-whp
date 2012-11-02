package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.junit.Assert.assertEquals;

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
}
