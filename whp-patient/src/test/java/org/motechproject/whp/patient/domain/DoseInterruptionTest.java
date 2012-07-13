package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class DoseInterruptionTest {

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

}
