package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;
import static org.motechproject.whp.patient.builder.PatientBuilder.*;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_PROVIDER_ID;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_TB_ID;

public class RecordAdherenceTestPart extends WHPAdherenceServiceTestPart {

    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();

        adherenceService.recordAdherence(adherenceSummary, auditParams);

        TreatmentWeek treatmentWeek = adherenceSummary.getWeek();
        List<Adherence> adherenceList = adherenceService.findLogsInRange(patient.getPatientId(), patient.currentTherapyId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        assertEquals(3, adherenceList.size());
        // TODO : Write more asserts to check meta on saved adherence
    }

    @Test
    public void shouldOverwritePillStatusForAdherenceUpdate() {
        patientService.createPatient(new PatientRequestBuilder().withDefaults().build());

        WeeklyAdherenceSummary threeDosesTaken = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        adherenceService.recordAdherence(threeDosesTaken, auditParams);

        WeeklyAdherenceSummary zeroDosesTaken = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        adherenceService.recordAdherence(zeroDosesTaken, auditParams);

        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        WeeklyAdherenceSummary currentWeekAdherence = adherenceService.currentWeekAdherence(patient);
        assertEquals(0, currentWeekAdherence.getDosesTaken());
    }

    @Test
    public void shouldSetLatestTbIdAndProviderIdOnAdherenceUpdate() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        WeeklyAdherenceSummary weeklyAdherenceSummary = recordAdherence();
        assertValidAdherence(weeklyAdherenceSummary, TB_ID, PROVIDER_ID);

        patientService.update(new PatientRequestBuilder().withDefaults().withMandatoryFieldsForCloseTreatment().build());
        patientService.update(new PatientRequestBuilder().withDefaults().withMandatoryFieldsForTransferInTreatment().withDateModified(DateUtil.now().minusDays(10)).build());
        recordAdherence();

        assertValidAdherence(weeklyAdherenceSummary, NEW_TB_ID, NEW_PROVIDER_ID);
    }

    private void assertValidAdherence(WeeklyAdherenceSummary weeklyAdherenceSummary, String tbId, String providerId) {
        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        List<Adherence> adherenceList = adherenceService.findLogsInRange(PatientBuilder.PATIENT_ID, patient.currentTherapyId(), weeklyAdherenceSummary.getWeek().startDate(), weeklyAdherenceSummary.getWeek().endDate());
        assertTbAndProviderId(adherenceList.get(0), tbId, providerId);
    }

    @Test
    public void shouldResetDoseStartDateWhenNoAdherenceIsPostedForSameWeek() {
        adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(1));

        adherenceService.recordAdherence(new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build(), auditParams);
        assertNull(allPatients.findByPatientId(PATIENT_ID).getCurrentTherapy().getStartDate());
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        Patient patient = createPatient(withDosesOnMonWedFri);

        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary expectedAdherenceSummary = new WeeklyAdherenceSummaryBuilder()
                .forPatient(patient)
                .build();
        adherenceService.recordAdherence(expectedAdherenceSummary, auditParams);

        WeeklyAdherenceSummary adherenceSummary = adherenceService.currentWeekAdherence(patient);
        areSame(expectedAdherenceSummary, adherenceSummary);
    }

    @Test
    public void shouldHaveZeroTakenDosesWhenCurrentWeekAdherenceIsNotCaptured() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());
        assertThat(adherenceService.currentWeekAdherence(patient).getDosesTaken(), is(0));
    }

    @Test
    public void shouldAddOrUpdateAdherenceData() {
        Adherence log1 = createLog(PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PATIENT_ID, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, PATIENT_ID);
        List<AdherenceLog> logs = allAdherenceLogs.getAll();
        assertEquals(2, logs.size());
        assertEquals(PATIENT_ID, logs.get(0).externalId());
        assertEquals(PATIENT_ID, logs.get(1).externalId());
    }

    @Test
    public void shouldCountTakenDosesForPatientAndTherapyBetweenGivenDates() {
        Adherence log1 = createLog(PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.NotTaken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PATIENT_ID, new LocalDate(2012, 1, 3), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(PATIENT_ID, new LocalDate(2012, 1, 5), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, PATIENT_ID);
        int dosesTaken = adherenceService.countOfDosesTakenBetween(PATIENT_ID, THERAPY_DOC_ID, new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 10));
        assertEquals(2, dosesTaken);
    }

    @Test
    public void shouldFindLogsByDateRangeForPatient() {
        String patientId = "patientId";
        Adherence log1 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3, log4);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patientId);

        List<Adherence> result = adherenceService.findLogsInRange(patientId, THERAPY_DOC_ID, new LocalDate(2012, 1, 1), new LocalDate(2012, 2, 1));
        assertEquals(2, result.size());
        assertEquals("tbid1", result.get(0).getTbId());
        assertEquals("tbid2", result.get(1).getTbId());
    }

    @Test
    public void shouldGetNthTakenDoseForPatientAndTherapy() {
        String patientId = "patientId";
        Adherence log1 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3, log4);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patientId);

        AdherenceRecord adherenceRecord = adherenceService.nThTakenDose(patientId, THERAPY_DOC_ID, 3, new LocalDate(2012, 1, 1));
        assertEquals(new LocalDate(2012, 3, 1), adherenceRecord.doseDate());
    }

    @Test
    public void shouldGetSortedAdherence() {
        String patientId = "patientId";
        Adherence log1 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        adherenceService.addOrUpdateLogsByDoseDate(asList(log1, log2, log3, log4), patientId);

        AdherenceList adherenceSortedByDate = adherenceService.getAdherenceSortedByDate(patientId, THERAPY_DOC_ID);

        assertEquals(3, adherenceSortedByDate.size());
        assertEquals(new LocalDate(2012, 1, 1), adherenceSortedByDate.get(0).getPillDate());
        assertEquals(new LocalDate(2012, 3, 1), adherenceSortedByDate.get(2).getPillDate());
    }

    @Test
    public void shouldReturnTrueIfPatientHasAdherenceInLastWeek() {
        String patientId = "patientId";
        //Week -> Monday to NEXT Saturday. So, DateUtil.today (which is mocked to saturday) belongs to week starting LAST Monday.
        Adherence log1 = createLog(patientId, today.minusDays(6), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patientId);

        boolean hasTakenAdherenceInLastWeek = adherenceService.isAdherenceRecordedForCurrentWeek(patientId, THERAPY_DOC_ID);
        assertTrue(hasTakenAdherenceInLastWeek);
    }

    @Test
    public void shouldReturnFalseIfPatientDoesNotHaveAdherenceInLastWeek() {
        String patientId = "patientId";
        //Adherence log on Sunday before LAST Monday => does not belong to this week.
        Adherence log1 = createLog(patientId, today.minusDays(13), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patientId);

        boolean hasTakenAdherenceInLastWeek = adherenceService.isAdherenceRecordedForCurrentWeek(patientId, THERAPY_DOC_ID);
        assertFalse(hasTakenAdherenceInLastWeek);
    }
}
