package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Treatment treatment = new Treatment();
        treatment.close(now());
        assertEquals(today(), treatment.getCloseDate());
    }

    @Test
    public void shouldPauseTreatment() {
        Treatment treatment = new Treatment();
        DateTime now = now();
        treatment.pause("paws", now);
        assertTrue(treatment.isPaused());
    }

    @Test
    public void shouldRestartTreatment() {
        Treatment treatment = new Treatment();
        DateTime now = now();
        treatment.pause("paws", now);
        treatment.resume("swap", now);
        assertFalse(treatment.isPaused());
    }
}
