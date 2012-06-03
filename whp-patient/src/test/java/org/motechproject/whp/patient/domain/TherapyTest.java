package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TherapyTest {

    @Test
    public void shouldCloseTreatment() {
        Therapy therapy = new Therapy();
        therapy.close(now());
        assertEquals(today(), therapy.getCloseDate());
    }

}
