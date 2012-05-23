package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.util.DateUtil.tomorrow;

public class TreatmentInterruptionsTest {

    @Test
    public void isTreatmentInterruptedReturnsFalse_IfNoInterruptionsExist() {
        assertTrue(new TreatmentInterruptions().getPauseReasons(asList(today())).isEmpty());
        assertTrue(new TreatmentInterruptions().getPauseReasons(asList(today().minusDays(6))).isEmpty());
    }

    @Test
    public void shouldReturnListOfAllPauseReasons_ForAllInterruptionsInGivenPeriod() {
        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", today());
        interruption1.resumeTreatment("resuming paws", today().plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", today().plusDays(2));
        interruption2.resumeTreatment("resuming paws", today().plusDays(3));
        TreatmentInterruption interruption3 = new TreatmentInterruption("pawsAgain", today().plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2, interruption3));

        List<LocalDate> justToday = asList(today());
        List<LocalDate> fromTodayToTomorrow = asList(today(), tomorrow());
        List<LocalDate> fromTodayToDayAfterTomorrow = asList(today(), tomorrow(), tomorrow().plusDays(1));
        List<LocalDate> fromTodayToThreeDaysAhead = asList(today(), tomorrow(), tomorrow().plusDays(1), tomorrow().plusDays(2));

        assertEquals(asList("paws"), treatmentInterruptions.getPauseReasons(justToday));
        assertEquals(asList("paws"), treatmentInterruptions.getPauseReasons(fromTodayToTomorrow));
        assertEquals(asList("paws", "paws"), treatmentInterruptions.getPauseReasons(fromTodayToDayAfterTomorrow));
        assertEquals(asList("paws", "paws", "pawsAgain"), treatmentInterruptions.getPauseReasons(fromTodayToThreeDaysAhead));
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
