package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class DoseInterruptionsTest {

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

}
