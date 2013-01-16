package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class DoseInterruptionsTest {

    final List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Thursday, DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Sunday);

    @Test
    public void shouldAddDoseInterruptionToDoseInterruptions() {
        DoseInterruptions doseInterruptions = new DoseInterruptions();
        DoseInterruption doseInterruption = new DoseInterruption(new LocalDate());

        doseInterruptions.add(doseInterruption);

        assertTrue(doseInterruptions.contains(doseInterruption));
    }

    @Test
    public void shouldSortDoseInterruptionsInAscendingOrderOfStartDates() {

        DoseInterruption doseInterruption1 = new DoseInterruption(new LocalDate(2012,7,2));
        DoseInterruption doseInterruption2 = new DoseInterruption(new LocalDate(2012,7,6));
        DoseInterruption doseInterruption3 = new DoseInterruption(new LocalDate(2012,7,4));

        DoseInterruptions doseInterruptions = new DoseInterruptions(Arrays.asList(doseInterruption1, doseInterruption2, doseInterruption3));

        assertArrayEquals(new DoseInterruption[]{doseInterruption1, doseInterruption3, doseInterruption2}, doseInterruptions.toArray());
    }


    @Test
    public void shouldReturnCumulativeMissedDoseCount() {
        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek);

        DoseInterruption doseInterruption1 = new DoseInterruption(new LocalDate(2012, 7, 2));
        doseInterruption1.endMissedPeriod(new LocalDate(2012, 7, 21));   //20

        DoseInterruption doseInterruption2 = new DoseInterruption(new LocalDate(2012,7,24));
        doseInterruption2.endMissedPeriod(new LocalDate(2012,7,26)); //3

        DoseInterruption doseInterruption3 = new DoseInterruption(new LocalDate(2013,1,24));
        doseInterruption3.endMissedPeriod(new LocalDate(2013,1,29));    //6

        DoseInterruptions doseInterruptions = new DoseInterruptions(Arrays.asList(doseInterruption1, doseInterruption2, doseInterruption3));

        assertEquals(29, doseInterruptions.getCumulativeMissedDoseCount(treatmentCategory,new LocalDate(2012, 7, 2)));
    }

    @Test
    public void shouldReturnOngoingDoseInterruptionIfExists() {
        DoseInterruption doseInterruption1 = new DoseInterruption(new LocalDate(2012, 7, 2));
        doseInterruption1.endMissedPeriod(new LocalDate(2012, 7, 21));
        DoseInterruption doseInterruption2 = new DoseInterruption(new LocalDate(2012,7,24));

        DoseInterruptions doseInterruptions = new DoseInterruptions(Arrays.asList(doseInterruption1, doseInterruption2));

        assertEquals(doseInterruption2, doseInterruptions.ongoingDoseInterruption());
        assertNull(new DoseInterruptions().ongoingDoseInterruption());
        assertNull(new DoseInterruptions(Arrays.asList(doseInterruption1)).ongoingDoseInterruption());
    }
}
