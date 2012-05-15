package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.today;

public class TreatmentInterruptionTest {

    @Test
    public void pauseTreatmentSetsReasonAndDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        assertTrue(interruption.isCurrentlyPaused());
        assertEquals("paws", interruption.getReasonForPause());
        assertEquals(today(), interruption.getPauseDate());
    }

    @Test
    public void resumeTreatmentSetsReasonAndDate() {
        LocalDate tomorrow = today().plusDays(1);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());

        interruption.resumeTreatment("swap", tomorrow);

        assertFalse(interruption.isCurrentlyPaused());
        assertEquals("swap", interruption.getReasonForResumption());
        assertEquals(tomorrow, interruption.getResumptionDate());
    }

}
