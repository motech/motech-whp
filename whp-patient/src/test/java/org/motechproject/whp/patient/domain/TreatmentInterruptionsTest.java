package org.motechproject.whp.patient.domain;

import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.util.DateUtil.today;

public class TreatmentInterruptionsTest {

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfNoInterruptionsExist() {
        assertTrue(new TreatmentInterruptions().getPauseReasons(today(), today()).isEmpty());
        assertTrue(new TreatmentInterruptions().getPauseReasons(today().minusDays(6), today()).isEmpty());
    }

    @Test
    public void shouldReturnListOfAllPauseReasons_ForAllInterruptionsInGivenPeriod() {
        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", today());
        interruption1.resumeTreatment("resuming paws", today().plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", today().plusDays(2));
        interruption2.resumeTreatment("resuming paws", today().plusDays(3));
        TreatmentInterruption interruption3 = new TreatmentInterruption("pawsAgain", today().plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2, interruption3));

        assertEquals("paws", treatmentInterruptions.getPauseReasons(today(), today()).get(0));
        assertEquals(asList("paws"), treatmentInterruptions.getPauseReasons(today(), today().plusDays(1)));
        assertEquals(asList("paws", "paws"), treatmentInterruptions.getPauseReasons(today(), today().plusDays(2)));
        assertEquals(asList("paws", "paws", "pawsAgain"), treatmentInterruptions.getPauseReasons(today(), today().plusDays(3)));
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
