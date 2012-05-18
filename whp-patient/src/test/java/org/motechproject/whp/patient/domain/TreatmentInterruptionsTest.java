package org.motechproject.whp.patient.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.util.DateUtil.today;

public class TreatmentInterruptionsTest {

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfNoInterruptionsExist() {
        assertNull(new TreatmentInterruptions().getPauseReason(today()));
        assertNull(new TreatmentInterruptions().getPauseReason(today().minusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfPillDate_IsInterrupted() {
        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", today());
        interruption1.resumeTreatment("resuming paws", today().plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", today().plusDays(2));
        interruption2.resumeTreatment("resuming paws", today().plusDays(3));
        TreatmentInterruption interruption3 = new TreatmentInterruption("pawsAgain", today().plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2, interruption3));

        assertEquals("paws", treatmentInterruptions.getPauseReason(today()));
        assertNull(treatmentInterruptions.getPauseReason(today().plusDays(1)));
        assertEquals("paws", treatmentInterruptions.getPauseReason(today().plusDays(2)));
        assertEquals("pawsAgain", treatmentInterruptions.getPauseReason(today().plusDays(3)));
    }

    @Test
    public void shouldReturnLastInterruptedTreatment() {
        assertNull(new TreatmentInterruptions().latestInterruption());

        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", today());
        interruption1.resumeTreatment("swap", today().plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", today().plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2));

        assertTrue(treatmentInterruptions.latestInterruption().isCurrentlyPaused());
    }
}
