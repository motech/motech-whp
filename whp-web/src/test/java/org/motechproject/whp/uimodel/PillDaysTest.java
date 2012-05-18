package org.motechproject.whp.uimodel;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.model.DayOfWeek.*;

public class PillDaysTest {

    TreatmentCategory threeDaysAWeek = new TreatmentCategory();
    TreatmentCategory sevenDaysAWeek = new TreatmentCategory();

    @Before
    public void setUp() {
        threeDaysAWeek.setPillDays(asList(Monday, Wednesday, Friday));
        sevenDaysAWeek.setPillDays(asList(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday));
    }

    @Test
    public void shouldReturnOneDoseAsTaken() {
        assertEquals(asList(Friday), PillDays.takenDays(threeDaysAWeek, 1));
        assertEquals(asList(Sunday), PillDays.takenDays(sevenDaysAWeek, 1));
    }

    @Test
    public void shouldReturnAllDosesAreTaken() {
        assertEquals(threeDaysAWeek.getPillDays(), PillDays.takenDays(threeDaysAWeek, 3));
        assertEquals(sevenDaysAWeek.getPillDays(), PillDays.takenDays(sevenDaysAWeek, 7));
    }
}