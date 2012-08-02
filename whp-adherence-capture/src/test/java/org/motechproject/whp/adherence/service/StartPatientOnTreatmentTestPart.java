package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class StartPatientOnTreatmentTestPart extends WHPAdherenceServiceTestPart {
    @Test
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        adherenceIsRecordedForTheFirstTime();
        LocalDate startDate = allPatients.findByPatientId(PATIENT_ID).getCurrentTherapy().getStartDate();
        assertEquals(
                today.withDayOfWeek(DayOfWeek.Monday.getValue()).minusWeeks(1),
                startDate
        );
    }

    @Test
    public void shouldNotStartPatientOnTreatmentWhenRecordingAdherenceForAnyWeekSubsequentToFirstEverAdherenceCapturedWeek() {
        adherenceIsRecordedForTheFirstTime();
        LocalDate startDate = allPatients.findByPatientId(PATIENT_ID).getCurrentTherapy().getStartDate();
        assertNotNull(startDate);

        mockCurrentDate(today.plusDays(3)); //moving to the sunday -> capturing adherence for this week -> subsequent to first ever adherence captured week
        adherenceService.recordWeeklyAdherence(new WeeklyAdherenceSummaryBuilder().build(), auditParams);

        assertEquals(
                startDate,
                allPatients.findByPatientId(PATIENT_ID).getCurrentTherapy().getStartDate()
        );
    }
}
