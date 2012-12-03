package org.motechproject.whp.it.adherence.capture.service;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.criteria.TherapyStartCriteria;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StartPatientOnTreatmentTestPart extends WHPAdherenceServiceTestPart {
    @Test
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        adherenceIsRecordedForTheFirstTime();
        LocalDate startDate = allPatients.findByPatientId(PatientBuilder.PATIENT_ID).getCurrentTherapy().getStartDate();
        assertEquals(
                today.withDayOfWeek(DayOfWeek.Monday.getValue()).minusWeeks(1),
                startDate
        );
    }

    @Test
    public void shouldNotStartPatientOnTreatmentWhenRecordingAdherenceForAnyWeekSubsequentToFirstEverAdherenceCapturedWeek() {
        adherenceIsRecordedForTheFirstTime();
        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        LocalDate startDate = patient.getCurrentTherapy().getStartDate();
        assertNotNull(startDate);

        mockCurrentDate(today.plusDays(3)); //moving to the sunday -> capturing adherence for this week -> subsequent to first ever adherence captured week
        final WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        Assert.assertEquals(
                startDate,
                allPatients.findByPatientId(PatientBuilder.PATIENT_ID).getCurrentTherapy().getStartDate()
        );
    }
}
