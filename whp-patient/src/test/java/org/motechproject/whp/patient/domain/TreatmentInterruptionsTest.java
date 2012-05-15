package org.motechproject.whp.patient.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.today;

public class TreatmentInterruptionsTest {

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfNoInterruptionsExist() {
        assertFalse(new TreatmentInterruptions().isTreatmentInterrupted(today()));
        assertFalse(new TreatmentInterruptions().isTreatmentInterrupted(today().minusDays(1)));
    }

    @Test
    public void isTreatmentInterruptedReturnsTrue_IfPillDate_IsInterrupted() {
        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", today());
        interruption1.resumeTreatment("swap", today().plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", today().plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2));

        assertTrue(treatmentInterruptions.isTreatmentInterrupted(today()));
        assertFalse(treatmentInterruptions.isTreatmentInterrupted(today().plusDays(1)));
        assertFalse(treatmentInterruptions.isTreatmentInterrupted(today().plusDays(2)));
        assertTrue(treatmentInterruptions.isTreatmentInterrupted(today().plusDays(3)));
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
