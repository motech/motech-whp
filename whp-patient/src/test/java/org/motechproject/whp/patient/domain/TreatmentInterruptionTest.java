package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.util.DateUtil;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.util.WHPDateUtil.getDatesInRange;

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

    /*Note: Week extends from Monday till the NEXT saturday. NOT the following/subsequent saturday, NEXT saturday*/

    @Test
    public void shouldReturnTrueIfTreatmentWasPausedAndRestartedInLastWeek() {
        LocalDate pauseDate = new LocalDate(2012, 5, 21);
        LocalDate resumptionDate = new LocalDate(2012, 5, 29);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);
        interruption.resumeTreatment("swap", resumptionDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 30));

        assertTrue(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnTrueIfTreatmentWasPausedInLastWeek() {
        LocalDate pauseDate = new LocalDate(2012, 5, 21);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 27));

        assertTrue(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnTrueIfTreatmentWasPausedInWeekBeforeLast() {
        LocalDate pauseDate = new LocalDate(2012, 5, 20);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 27));

        assertTrue(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnTrueIfTreatmentWasPausedInWeekBeforeLastAndResumedInLastWeek() {
        LocalDate pauseDate = new LocalDate(2012, 5, 20);
        LocalDate resumptionDate = new LocalDate(2012, 5, 21);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);
        interruption.resumeTreatment("swap", resumptionDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 27));

        assertTrue(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnTrueIfTreatmentWasPausedInWeekBeforeLastAndResumedInLastWeek_() {
        LocalDate pauseDate = new LocalDate(2012, 5, 20);
        LocalDate resumptionDate = new LocalDate(2012, 5, 21);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);
        interruption.resumeTreatment("swap", resumptionDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 27));

        assertTrue(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnFalseIfTreatmentWasPausedInWeekBeforeLastAndResumedInWeekBeforeLast() {
        LocalDate pauseDate = new LocalDate(2012, 5, 20);
        LocalDate resumptionDate = new LocalDate(2012, 5, 20);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);
        interruption.resumeTreatment("swap", resumptionDate);

        List<LocalDate> datesInWeek = getDatesInRange(new LocalDate(2012, 5, 21), new LocalDate(2012, 5, 27));

        Assert.assertFalse(interruption.isTreatmentInterrupted(datesInWeek));
    }

    @Test
    public void shouldReturnPausedDuration() {
        LocalDate pauseDate = new LocalDate(2012, 5, 20);
        LocalDate resumptionDate = new LocalDate(2012, 5, 24);
        TreatmentInterruption interruption = new TreatmentInterruption("paws", pauseDate);
        interruption.resumeTreatment("swap", resumptionDate);

        assertEquals(4, interruption.pausedDuration());

        TreatmentInterruption ongoingInterruption = new TreatmentInterruption("paws", DateUtil.today().minusDays(3));
        assertEquals(3, ongoingInterruption.pausedDuration());
    }
}
