package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PillTakenSummariesTest {

    private LocalDate today = new LocalDate(2012, 7, 11);

    @Test
    public void shouldSummarizeTotalDosesTillGivenDate() {
        PillTakenSummaries pillTakenSummaries = new PillTakenSummaries();
        pillTakenSummaries.setPillTakenCount(2, today);
        assertEquals(2, pillTakenSummaries.getTotalPillsTaken());
    }

    @Test
    public void shouldReturnSummaryTillTheSundayBeforeGivenDate_JustStarted() {
        PillTakenSummaries pillTakenSummaries = new PillTakenSummaries();
        pillTakenSummaries.setPillTakenCount(2, today);

        // have not yet finished current week
        assertEquals(0, pillTakenSummaries.getTotalPillsTakenTillLastSunday(today));
    }

    @Test
    public void shouldReturnSummaryTillTheSundayBeforeGivenDate_1WeekOld() {
        LocalDate nextWeekDate = today.plusWeeks(1);
        PillTakenSummaries pillTakenSummaries = new PillTakenSummaries();
        pillTakenSummaries.setPillTakenCount(2, today);
        assertEquals(2, pillTakenSummaries.getTotalPillsTakenTillLastSunday(nextWeekDate));
    }

    @Test
    public void shouldReturnSummaryTillTodayAsSummaryTillLastSundayOnASunday() {
        LocalDate sunday = new LocalDate(2012, 7, 15);
        PillTakenSummaries pillTakenSummaries = new PillTakenSummaries();
        pillTakenSummaries.setPillTakenCount(7, sunday);
        assertEquals(7, pillTakenSummaries.getTotalPillsTakenTillLastSunday(sunday));
    }
}
