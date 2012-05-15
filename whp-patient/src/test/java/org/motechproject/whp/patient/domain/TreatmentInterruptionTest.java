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

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfPillDateIsBeforePauseDate_AndCurrentTreatmentIsPaused() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        assertFalse(interruption.isTreatmentInterrupted(today().minusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfPillDateIsAfterPauseDate_AndCurrentTreatmentIsPaused() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        assertTrue(interruption.isTreatmentInterrupted(today()));
        assertTrue(interruption.isTreatmentInterrupted(today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfPillDateIsAfterResumptionDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().minusDays(1));
        interruption.resumeTreatment("swap", today());
        assertFalse(interruption.isTreatmentInterrupted(today()));
        assertFalse(interruption.isTreatmentInterrupted(today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfPillDateIsBeforeResumptionDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().minusDays(1));
        interruption.resumeTreatment("swap", today());
        assertTrue(interruption.isTreatmentInterrupted(today().minusDays(1)));
    }
}
