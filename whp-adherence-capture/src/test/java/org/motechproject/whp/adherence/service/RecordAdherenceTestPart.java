package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.*;
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
    Patient patient;
    String patientId;

    @Before
    public void setUp(){
        super.setup();
        patientId = "patientid";
        patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();

    }
    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();

        adherenceService.recordWeeklyAdherence(adherenceSummary, patient, auditParams);

        TreatmentWeek treatmentWeek = adherenceSummary.getWeek();
        List<Adherence> adherenceList = adherenceService.findLogsInRange(patient.getPatientId(), patient.currentTherapyId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        assertEquals(3, adherenceList.size());
        // TODO : Write more asserts to check meta on saved adherence
    }

    @Test
    public void shouldOverwritePillStatusForAdherenceUpdate() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());

        WeeklyAdherenceSummary threeDosesTaken = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        adherenceService.recordWeeklyAdherence(threeDosesTaken, patient, auditParams);

        WeeklyAdherenceSummary zeroDosesTaken = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        adherenceService.recordWeeklyAdherence(zeroDosesTaken, patient, auditParams);

        patient = allPatients.findByPatientId(PATIENT_ID);
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

        final WeeklyAdherenceSummary build = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        Patient patient = allPatients.findByPatientId(PATIENT_ID);

        adherenceService.recordWeeklyAdherence(build, patient, auditParams);

        patient = allPatients.findByPatientId(PATIENT_ID);
        assertNull(patient.getCurrentTherapy().getStartDate());
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        Patient patient = createPatient(withDosesOnMonWedFri);

        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary expectedAdherenceSummary = new WeeklyAdherenceSummaryBuilder()
                .forPatient(patient)
                .build();
        adherenceService.recordWeeklyAdherence(expectedAdherenceSummary, patient, auditParams);

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
        Patient patient = new PatientBuilder().withPatientId(PATIENT_ID).build();

        Adherence log1 = createLog(PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PATIENT_ID, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2);
        adherenceService.addOrUpdateLogsByDoseDate(adherences, patient, auditParams);
        List<AdherenceLog> logs = allAdherenceLogs.getAll();
        assertEquals(2, logs.size());
        assertEquals(PATIENT_ID, logs.get(0).externalId());
        assertEquals(PATIENT_ID, logs.get(1).externalId());
    }

    @Test
    public void shouldCountTakenDosesForPatientAndTherapyBetweenGivenDates() {
        Patient patient = new PatientBuilder().withPatientId(PATIENT_ID).build();

        Adherence log1 = createLog(PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.NotTaken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PATIENT_ID, new LocalDate(2012, 1, 3), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(PATIENT_ID, new LocalDate(2012, 1, 5), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3);

        adherenceService.addOrUpdateLogsByDoseDate(adherences,patient, auditParams);
        int dosesTaken = adherenceService.countOfDosesTakenBetween(PATIENT_ID, THERAPY_DOC_ID, new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 10));
        assertEquals(2, dosesTaken);
    }

    @Test
    public void shouldFindLogsByDateRangeForPatient() {

        Adherence log1 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3, log4);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patient, auditParams);

        List<Adherence> result = adherenceService.findLogsInRange(patientId, THERAPY_DOC_ID, new LocalDate(2012, 1, 1), new LocalDate(2012, 2, 1));
        assertEquals(2, result.size());
        assertEquals("tbid1", result.get(0).getTbId());
        assertEquals("tbid2", result.get(1).getTbId());
    }

    @Test
    public void shouldGetNthTakenDoseForPatientAndTherapy() {

        Adherence log1 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3, log4);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patient, auditParams);

        AdherenceRecord adherenceRecord = adherenceService.nThTakenDose(patientId, THERAPY_DOC_ID, 3, new LocalDate(2012, 1, 1));
        assertEquals(new LocalDate(2012, 3, 1), adherenceRecord.doseDate());
    }

    @Test
    @Ignore("failing in 1.2.0 couch")
    public void shouldGetSortedAdherence() {

        Adherence log1 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        adherenceService.addOrUpdateLogsByDoseDate(asList(log1, log2, log3, log4), patient, null);

        AdherenceList adherenceSortedByDate = adherenceService.getAdherenceSortedByDate(patientId, THERAPY_DOC_ID);

        assertEquals(3, adherenceSortedByDate.size());
        assertEquals(new LocalDate(2012, 1, 1), adherenceSortedByDate.get(0).getPillDate());
        assertEquals(new LocalDate(2012, 3, 1), adherenceSortedByDate.get(2).getPillDate());
    }

}
