package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class WeeklyAdherenceFormTest extends BaseUnitTest {

    WeeklyAdherenceForm weeklyAdherenceForm;

    @Before
    public void setUp() {
        weeklyAdherenceForm = createTestWeeklyAdherenceForm();
    }

    @Test
    public void shouldGet_ReasonForPause() {
        assertFalse(weeklyAdherenceForm.isTreatmentCurrentlyPaused());
        assertEquals("paws", weeklyAdherenceForm.getTreatmentPauseReasons());
    }

    @Test
    public void showsNoWarningMessage() {
        mockCurrentDate(new LocalDate(2012, 5, 13));  //Sunday
        LocalDate monday = new LocalDate(2012, 5, 7);      // no interruptions
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        weeklyAdherenceForm = new WeeklyAdherenceForm(weeklyAdherence, getPatientWith(new TreatmentInterruptions()));
        assertEquals("", weeklyAdherenceForm.getWarningMessage());
    }

    @Test
    public void warningMessageShows_ThePauseReason() {
        mockCurrentDate(new LocalDate(2012, 5, 13));    //  interruptions and Sunday
        assertEquals("This patient has been restarted on medicines on 08-05-2012 after being paused on 07-05-2012. Reasons for pause: paws", weeklyAdherenceForm.getWarningMessage());
    }

    @Test
    public void warningMessageShows_That_ProviderCannotUpdateAdherence() {
        mockCurrentDate(new LocalDate(2012, 5, 9));     // Wednesday
        LocalDate monday = new LocalDate(2012, 5, 7);   // no interruptions
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        weeklyAdherenceForm = new WeeklyAdherenceForm(weeklyAdherence, getPatientWith(new TreatmentInterruptions()));
        assertEquals("Please contact the CMF admin to update adherence.", weeklyAdherenceForm.getWarningMessage());
    }

    @Test
    public void warningMessageShows_That_ProviderCannotUpdateAdherence_AndReasonForPause() {
        mockCurrentDate(new LocalDate(2012, 5, 9));    // interruptions and Wednesday
        assertEquals("This patient has been restarted on medicines on 08-05-2012 after being paused on 07-05-2012. Reasons for pause: paws<br/>" +
                "Please contact the CMF admin to update adherence.", weeklyAdherenceForm.getWarningMessage());
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
        weeklyAdherenceForm = new WeeklyAdherenceForm(weeklyAdherence, getPatientWith(treatmentInterruptions));
        assertTrue(weeklyAdherenceForm.isTreatmentCurrentlyPaused());
        assertEquals("paws, pawsAgain", weeklyAdherenceForm.getTreatmentPauseReasons());
    }

    private WeeklyAdherenceForm createTestWeeklyAdherenceForm() {
        LocalDate monday = new LocalDate(2012, 5, 7);
        WeeklyAdherence weeklyAdherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(monday).build();
        TreatmentInterruption interruption = new TreatmentInterruption("paws", monday);
        interruption.resumeTreatment("swap", monday.plusDays(1));
        TreatmentInterruptions treatmentInterruptions = new TreatmentInterruptions(Arrays.asList(interruption));
        mockCurrentDate(new LocalDate(2012, 5, 10)); // Any day in the week of pause
        return new WeeklyAdherenceForm(weeklyAdherence, getPatientWith(treatmentInterruptions));
    }

    private Patient getPatientWith(TreatmentInterruptions treatmentInterruptions) {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentProvidedTreatment().setInterruptions(treatmentInterruptions);
        return patient;
    }
}
