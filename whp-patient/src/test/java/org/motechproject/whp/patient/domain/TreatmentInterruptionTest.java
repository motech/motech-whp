package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Ignore;
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
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsPaused_AndAsOfDateIsAfterPauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        assertTrue(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsPaused_AndAsOfDateIsOnPauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        assertTrue(interruption.isTreatmentInterrupted(today(), today()));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsPaused_AndPauseDateIsBeforeStartDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().minusDays(4));
        assertTrue(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfCurrentTreatmentIsPaused_AndAsOfDateIsBeforePauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().plusDays(1));
        assertFalse(interruption.isTreatmentInterrupted(today(), today()));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsNotPaused_AndPauseDateIsOnStartDate_AndAsOfDateIsOnPauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        interruption.resumeTreatment("swap", today());
        assertTrue(interruption.isTreatmentInterrupted(today(), today()));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsNotPaused_AndPauseDateIsAfterStartDate_AndAsOfDateIsOnPauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().plusDays(1));
        interruption.resumeTreatment("swap", today().plusDays(1));
        assertTrue(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsNotPaused_AndPauseDateIsOnStartDate_AndAsOfDateIsAfterPauseDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today());
        interruption.resumeTreatment("swap", today());
        assertTrue(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfCurrentTreatmentIsNotPaused_AndPauseDateIsAfterAsOfDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().plusDays(2));
        interruption.resumeTreatment("swap", today().plusDays(3)); //does not matter
        assertFalse(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfCurrentTreatmentIsNotPaused_AndPauseDateIsBeforeStartDate_AndActivationDateIsBetweenStartAndAsOfDates() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().minusDays(4));
        interruption.resumeTreatment("swap", today());
        assertTrue(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfCurrentTreatmentIsNotPaused_AndPauseDateIsBeforeStartDate_AndActivationDateIsBeforeStartDate() {
        TreatmentInterruption interruption = new TreatmentInterruption("paws", today().minusDays(4));
        interruption.resumeTreatment("swap", today().minusDays(3));
        assertFalse(interruption.isTreatmentInterrupted(today(), today().plusDays(1)));
    }
}
