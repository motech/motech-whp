package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class WeeklyAdherenceFormTest {

    WeeklyAdherenceForm weeklyAdherenceForm;

    @Before
    public void setUp() {
        weeklyAdherenceForm = createTestWeeklyAdherenceForm();
    }

    private WeeklyAdherenceForm createTestWeeklyAdherenceForm() {
        LocalDate monday = new LocalDate(2012, 5, 7);
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        TreatmentInterruption interruption = new TreatmentInterruption("paws", monday);
        interruption.resumeTreatment("swap", monday.plusDays(1));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption));

        return new WeeklyAdherenceForm(weeklyAdherence, treatmentInterruptions);
    }

    @Test
    public void shouldPopulate_IsTreatmentPausedFlag_OnAdherenceForm() {
        List<AdherenceForm> adherenceList = weeklyAdherenceForm.getAdherenceList();

        assertAdherenceLog(adherenceList.get(0), true, DayOfWeek.Monday);
        assertAdherenceLog(adherenceList.get(1), false, DayOfWeek.Wednesday);
        assertAdherenceLog(adherenceList.get(2), false, DayOfWeek.Friday);
    }

    @Test
    public void shouldReturnOnlyUpdatedAdherenceForms() {
        weeklyAdherenceForm.adherenceList.get(0).setIsNotTaken(true);
        weeklyAdherenceForm.adherenceList.get(1).setIsTaken(true);
        weeklyAdherenceForm.adherenceList.get(2).setIsNotTaken(true);

        WeeklyAdherence weeklyAdherence = weeklyAdherenceForm.updatedWeeklyAdherence();

        assertEquals(2, weeklyAdherence.getAdherenceLogs().size());
        assertEquals(DayOfWeek.Wednesday, weeklyAdherence.getAdherenceLogs().get(0).getPillDay());
        assertEquals(DayOfWeek.Friday, weeklyAdherence.getAdherenceLogs().get(1).getPillDay());
    }

    private void assertAdherenceLog(AdherenceForm adherenceForm, boolean expectedInterruption, DayOfWeek expectedDayOfWeek) {
        assertEquals(expectedDayOfWeek, adherenceForm.getPillDay());
        assertEquals(expectedInterruption, adherenceForm.isTreatmentInterrupted());
    }
}
