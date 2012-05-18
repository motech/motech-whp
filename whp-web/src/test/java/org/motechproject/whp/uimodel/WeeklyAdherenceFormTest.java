package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.motechproject.util.DateUtil.today;

public class WeeklyAdherenceFormTest {

    WeeklyAdherenceForm weeklyAdherenceForm;

    @Before
    public void setUp() {
        weeklyAdherenceForm = createTestWeeklyAdherenceForm();
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

    @Test
    public void shouldGet_ReasonsForPause() {
        assertTrue(weeklyAdherenceForm.isTreatmentPaused());
        assertEquals("paws", weeklyAdherenceForm.getTreatmentPauseReason());
    }

    @Test
    public void pauseReasonsShouldBeUnique() {
        LocalDate monday = new LocalDate(2012, 5, 7);
        TreatmentInterruption interruption1 = new TreatmentInterruption("paws", monday);
        interruption1.resumeTreatment("resuming paws", monday.plusDays(1));
        TreatmentInterruption interruption2 = new TreatmentInterruption("paws", monday.plusDays(2));
        interruption2.resumeTreatment("resuming paws", monday.plusDays(3));
        TreatmentInterruption interruption3 = new TreatmentInterruption("pawsAgain", monday.plusDays(3));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption1, interruption2, interruption3));

        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        weeklyAdherenceForm = new WeeklyAdherenceForm(weeklyAdherence, treatmentInterruptions);

        assertTrue(weeklyAdherenceForm.isTreatmentPaused());
        assertEquals("paws, pawsAgain", weeklyAdherenceForm.getTreatmentPauseReason());
    }

    private WeeklyAdherenceForm createTestWeeklyAdherenceForm() {
        LocalDate monday = new LocalDate(2012, 5, 7);
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        TreatmentInterruption interruption = new TreatmentInterruption("paws", monday);
        interruption.resumeTreatment("swap", monday.plusDays(1));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption));

        return new WeeklyAdherenceForm(weeklyAdherence, treatmentInterruptions);
    }

}
