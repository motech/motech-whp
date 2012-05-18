package org.motechproject.whp.adherence.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.util.AssertAdherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.*;

public class WeeklyAdherenceTest extends BaseUnitTest {

    @Test
    public void shouldReturnAdherenceTemplate() {
        Patient patient = new PatientBuilder().withDefaults().build();
        WeeklyAdherence adherence = WeeklyAdherence.createAdherenceFor(patient);
        AssertAdherence.forWeek(adherence, Monday, Wednesday, Friday);
    }


    @Test
    public void shouldSetCurrentWeekAs_CurrentTreatmentWeek_GivenTodayIsSunday() {
        mockCurrentDate(new LocalDate(2012, 5, 27));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsMonday() {
        mockCurrentDate(new LocalDate(2012, 5, 28));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 21), week.startDate());
        assertEquals(new LocalDate(2012, 5, 27), week.endDate());
    }

    @Test
    public void shouldSetLastWeekAs_CurrentTreatmentWeek_GivenTodayIsSaturday() {
        mockCurrentDate(new LocalDate(2012, 5, 26));
        TreatmentWeek week = new WeeklyAdherence().getWeek();
        assertEquals(new LocalDate(2012, 5, 14), week.startDate());
        assertEquals(new LocalDate(2012, 5, 20), week.endDate());
    }
}
