package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class WeeklyAdherenceFormTest {

    @Test
    public void shouldPopulate_IsTreatmentPausedFlag_OnAdherenceForm() {
        LocalDate monday = new LocalDate(2012, 5, 7);
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        TreatmentInterruption interruption = new TreatmentInterruption("paws", monday);
        interruption.resumeTreatment("swap", monday.plusDays(1));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption));

        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(weeklyAdherence, treatmentInterruptions);
        List<AdherenceForm> adherenceList = weeklyAdherenceForm.getAdherenceList();

        assertAdherenceLog(adherenceList.get(0), true, DayOfWeek.Monday);
        assertAdherenceLog(adherenceList.get(1), false, DayOfWeek.Wednesday);
        assertAdherenceLog(adherenceList.get(2), false, DayOfWeek.Friday);
    }

    private void assertAdherenceLog(AdherenceForm adherenceForm, boolean expectedInterruption, DayOfWeek expectedDayOfWeek) {
        assertEquals(expectedDayOfWeek, adherenceForm.getPillDay());
        assertEquals(expectedInterruption, adherenceForm.isTreatmentInterrupted());
    }
}
