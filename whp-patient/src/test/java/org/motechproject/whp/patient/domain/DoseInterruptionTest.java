package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class DoseInterruptionTest extends BaseUnitTest {

    final List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Thursday, DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Sunday);

    @Test
    public void shouldSetStartDateOnDoseInterruption() {
        LocalDate startDate = new LocalDate();
        DoseInterruption doseInterruption = new DoseInterruption(startDate);

        assertEquals(startDate, doseInterruption.startDate());
    }

    @Test
    public void shouldSetEndDateOnDoseInterruption() {
        LocalDate startDate = new LocalDate();
        LocalDate endDate = startDate.plusDays(2);
        DoseInterruption doseInterruption = new DoseInterruption(startDate);
        doseInterruption.endMissedPeriod(endDate);

        assertEquals(endDate, doseInterruption.endDate());
    }

    @Test
    public void shouldReturnNumberOfMissedDosesBetweenStartAndEndDatesForGivenCategory() {
        LocalDate startDate = new LocalDate(2012, 7, 2);
        LocalDate endDate = new LocalDate(2012, 7, 21);
        DoseInterruption doseInterruption = new DoseInterruption(startDate);
        doseInterruption.endMissedPeriod(endDate);

        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek);

        assertEquals(20, doseInterruption.getMissedDoseCount(treatmentCategory));
    }

    @Test
    public void shouldReturnNumberOfMissedDosesForGivenCategoryAndStartDateAndEndDate() {
        LocalDate startDate = new LocalDate(2012, 7, 2);
        LocalDate endDate = new LocalDate(2012, 7, 21);
        DoseInterruption doseInterruption = new DoseInterruption(startDate);
        doseInterruption.endMissedPeriod(endDate);

        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek);

        assertEquals(20, doseInterruption.getMissedDoseCount(treatmentCategory, new LocalDate(2012, 7, 2), endDate));

        assertEquals(13, doseInterruption.getMissedDoseCount(treatmentCategory, new LocalDate(2012, 7, 2), endDate.minusWeeks(1)));
        assertEquals(20, doseInterruption.getMissedDoseCount(treatmentCategory, new LocalDate(2012, 7, 1), endDate));
        assertEquals(0, doseInterruption.getMissedDoseCount(treatmentCategory, new LocalDate(2012, 8, 1), endDate));
    }

    @Test
    public void shouldReturnNumberOfMissedDosesForGivenCategoryUsingCurrentAdherenceWeekEndDate() {
        LocalDate startDate = new LocalDate(2012, 7, 2);
        DoseInterruption doseInterruption = new DoseInterruption(startDate);

        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek);

        mockCurrentDate(new LocalDate(2012, 7, 22));
        assertEquals(21, doseInterruption.getMissedDoseCount(treatmentCategory));

        mockCurrentDate(new LocalDate(2012, 7, 28));
        assertEquals(21, doseInterruption.getMissedDoseCount(treatmentCategory));
    }
}
