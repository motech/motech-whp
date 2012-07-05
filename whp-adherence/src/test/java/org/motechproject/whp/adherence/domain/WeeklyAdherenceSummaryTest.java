package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.common.TreatmentWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.common.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherenceSummaryTest extends BaseUnitTest {

    @Test
    public void shouldReturnAdherenceTemplate() {
        Patient patient = new PatientBuilder().withDefaults().build();
        WeeklyAdherenceSummary adherenceSummary = WeeklyAdherenceSummary.forFirstWeek(patient);
        assertEquals(patient.getPatientId(), adherenceSummary.getPatientId());
        assertEquals(0, adherenceSummary.getDosesTaken());
        assertEquals(currentWeekInstance(), adherenceSummary.getWeek());
        assertEquals(0, adherenceSummary.takenDays(patient.getCurrentTherapy().getTreatmentCategory()).size());
    }

    @Test
    public void shouldReturnWeeklyAdherenceSummaryWhenDosesAreTaken() {
        Patient patient = new PatientBuilder().withDefaults().build();
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        assertEquals(patient.getPatientId(), adherenceSummary.getPatientId());
        assertEquals(3, adherenceSummary.getDosesTaken());
        assertEquals(currentWeekInstance(), adherenceSummary.getWeek());
        assertEquals(Arrays.asList(Monday, Wednesday, Friday), adherenceSummary.takenDays(patient.getCurrentTherapy().getTreatmentCategory()));
    }

    @Test
    public void shouldSetCurrentWeekAs_CurrentTreatmentWeek_GivenTodayIsSunday() {
        mockCurrentDate(new LocalDate(2012, 5, 27));
        TreatmentWeek week = new WeeklyAdherenceSummary().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsMonday() {
        mockCurrentDate(new LocalDate(2012, 5, 28));
        TreatmentWeek week = new WeeklyAdherenceSummary().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsSaturday() {
        mockCurrentDate(new LocalDate(2012, 5, 26));
        TreatmentWeek week = new WeeklyAdherenceSummary().getWeek();
        assertEquals(new LocalDate(2012, 5, 14), week.startDate());
        assertEquals(new LocalDate(2012, 5, 20), week.endDate());
    }
}
