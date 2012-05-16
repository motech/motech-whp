package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.refdata.domain.TreatmentStatus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Treatment treatment = new Treatment();
        treatment.close("Cured", now());
        assertEquals(TreatmentOutcome.Cured, treatment.getTreatmentOutcome());
        assertEquals(TreatmentStatus.Closed, treatment.getStatus());
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
