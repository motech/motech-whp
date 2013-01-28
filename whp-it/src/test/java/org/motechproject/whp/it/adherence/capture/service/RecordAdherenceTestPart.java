package org.motechproject.whp.it.adherence.capture.service;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.criteria.TherapyStartCriteria;
import org.motechproject.whp.adherence.domain.*;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.adherence.util.AssertAdherence;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class RecordAdherenceTestPart extends WHPAdherenceServiceTestPart {
    Patient patient;
    String patientId;

    @Before
    public void setUp() {
        super.setup();
        patientId = "patientid";
        patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();

    }

    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        TreatmentWeek treatmentWeek = weeklyAdherenceSummary.getWeek();
        List<Adherence> adherenceLogs = adherenceService.findLogsInRange(patient.getPatientId(), patient.currentTherapyId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        assertEquals(3, adherenceLogs.size());
    }

    @Test
    public void shouldOverwritePillStatusForAdherenceUpdate() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());

        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        WeeklyAdherenceSummary currentWeekAdherence = adherenceService.currentWeekAdherence(patient);
        Assert.assertEquals(0, currentWeekAdherence.getDosesTaken());
    }

    @Test
    public void shouldSetLatestTbIdAndProviderIdOnAdherenceUpdate() {
        PatientRequest request = new PatientRequestBuilder().withDefaults().build();
        createPatient(request);
        startTreatment(request);

        WeeklyAdherenceSummary weeklyAdherenceSummary = recordAdherence();
        assertValidAdherence(weeklyAdherenceSummary, PatientBuilder.TB_ID, PatientBuilder.PROVIDER_ID);

        patientWebService.update(new PatientRequestBuilder().withDefaults().withMandatoryFieldsForCloseTreatment().build());
        patientWebService.update(new PatientRequestBuilder().withDefaults().withMandatoryFieldsForTransferInTreatment().withTbRegistrationDate(DateUtil.now().minusDays(10)).withDateModified(DateUtil.now().minusDays(10)).build());
        recordAdherence();

        assertValidAdherence(weeklyAdherenceSummary, PatientRequestBuilder.NEW_TB_ID, PatientRequestBuilder.NEW_PROVIDER_ID);
    }

    private void assertValidAdherence(WeeklyAdherenceSummary weeklyAdherenceSummary, String tbId, String providerId) {
        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        List<Adherence> adherenceList = adherenceService.findLogsInRange(PatientBuilder.PATIENT_ID, patient.currentTherapyId(), weeklyAdherenceSummary.getWeek().startDate(), weeklyAdherenceSummary.getWeek().endDate());
        assertTbAndProviderId(adherenceList.get(0), tbId, providerId);
        assertEquals(patient.getCurrentTreatment().getProviderDistrict(), adherenceList.get(0).getDistrict());
    }

    @Test
    public void shouldResetDoseStartDateWhenNoAdherenceIsPostedForSameWeek() {
        adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(1));

        final WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
            allPatients.update(patient);
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        assertNull(patient.getCurrentTherapy().getStartDate());
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        Patient patient = createPatient(withDosesOnMonWedFri);

        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder()
                .forPatient(patient)
                .build();
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (TherapyStartCriteria.shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        WeeklyAdherenceSummary adherenceSummary = adherenceService.currentWeekAdherence(patient);
        AssertAdherence.areSame(weeklyAdherenceSummary, adherenceSummary);
    }

    @Test
    public void shouldHaveZeroTakenDosesWhenCurrentWeekAdherenceIsNotCaptured() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());
        assertThat(adherenceService.currentWeekAdherence(patient).getDosesTaken(), is(0));
    }

    @Test
    public void shouldAddOrUpdateAdherenceData() {
        Patient patient = new PatientBuilder().withPatientId(PatientBuilder.PATIENT_ID).build();

        Adherence log1 = createLog(PatientBuilder.PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PatientBuilder.PATIENT_ID, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2);
        adherenceService.addOrUpdateLogsByDoseDate(adherences, patient, auditParams);
        List<AdherenceLog> logs = allAdherenceLogs.getAll();
        assertEquals(2, logs.size());
        Assert.assertEquals(PatientBuilder.PATIENT_ID, logs.get(0).externalId());
        Assert.assertEquals(PatientBuilder.PATIENT_ID, logs.get(1).externalId());
    }

    @Test
    public void shouldCountTakenDosesForPatientAndTherapyBetweenGivenDates() {
        Patient patient = new PatientBuilder().withPatientId(PatientBuilder.PATIENT_ID).build();

        Adherence log1 = createLog(PatientBuilder.PATIENT_ID, new LocalDate(2012, 1, 1), PillStatus.NotTaken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(PatientBuilder.PATIENT_ID, new LocalDate(2012, 1, 3), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(PatientBuilder.PATIENT_ID, new LocalDate(2012, 1, 5), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        List<Adherence> adherences = asList(log1, log2, log3);

        adherenceService.addOrUpdateLogsByDoseDate(adherences, patient, auditParams);
        int dosesTaken = adherenceService.countOfDosesTakenBetween(PatientBuilder.PATIENT_ID, THERAPY_DOC_ID, new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 10));
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
        Assert.assertEquals("tbid1", result.get(0).getTbId());
        Assert.assertEquals("tbid2", result.get(1).getTbId());
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
        Assert.assertEquals(new LocalDate(2012, 3, 1), adherenceRecord.doseDate());
    }

    @Test
    public void shouldGetSortedAdherence() {

        Adherence log1 = createLog(patientId, new LocalDate(2012, 3, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log2 = createLog(patientId, new LocalDate(2012, 2, 1), PillStatus.Taken, "tbid2", THERAPY_DOC_ID, "providerId1");
        Adherence log3 = createLog(patientId, new LocalDate(2012, 1, 1), PillStatus.Taken, "tbid1", THERAPY_DOC_ID, "providerId1");
        Adherence log4 = createLog(patientId, new LocalDate(2012, 1, 13), PillStatus.Taken, "tbid2", "diffTherapy", "providerId1");
        adherenceService.addOrUpdateLogsByDoseDate(asList(log1, log2, log3, log4), patient, new AuditParams("test", AdherenceSource.WEB, "test"));

        AdherenceList adherenceSortedByDate = adherenceService.getAdherenceSortedByDate(patientId, THERAPY_DOC_ID);

        Assert.assertEquals(3, adherenceSortedByDate.size());
        Assert.assertEquals(new LocalDate(2012, 1, 1), adherenceSortedByDate.get(0).getPillDate());
        Assert.assertEquals(new LocalDate(2012, 3, 1), adherenceSortedByDate.get(2).getPillDate());
    }

}
