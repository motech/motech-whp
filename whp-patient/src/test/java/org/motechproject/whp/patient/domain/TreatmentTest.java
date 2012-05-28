package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Treatment treatment = new Treatment();
        treatment.close(now());
        assertEquals(today(), treatment.getCloseDate());
    }

}
