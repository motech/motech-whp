package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AllAuditLogs;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.*;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;
import static org.motechproject.whp.patient.builder.PatientBuilder.*;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_PROVIDER_ID;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_TB_ID;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceIT extends SpringIntegrationTest {

    LocalDate today = DateUtil.newDate(2012, 5, 3);

    @Autowired
    @Qualifier(value = "whpDbConnector")
    CouchDbConnector couchDbConnector;

    @Autowired
    @Qualifier(value = "adherenceDbConnector")
    CouchDbConnector adherenceDbConnector;
    @Autowired
    private WHPAdherenceService adherenceService;

    @Autowired
    private PatientService patientService;
    @Autowired
    private AllAdherenceLogs allAdherenceLogs;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private AllTherapies allTherapies;
    @Autowired
    private AllAuditLogs allAuditLogs;

    private final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");
    private final String THERAPY_DOC_ID = "THERAPY_DOC_ID";


    @Before
    public void setup() {
        mockCurrentDate(today);
    }

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
    @Ignore("Test should be moved")
    public void shouldSetLatestTbIdAndProviderIdOnAdherenceUpdate() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        WeeklyAdherenceSummary weeklyAdherenceSummary = recordAdherence();
        assertValidAdherence(weeklyAdherenceSummary, TB_ID, PROVIDER_ID);

        patientService.update(new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build());
        patientService.update(new PatientRequestBuilder().withMandatoryFieldsForTransferInTreatment().withDateModified(DateUtil.now().minusDays(10)).build());
        recordAdherence();

        assertValidAdherence(weeklyAdherenceSummary, NEW_TB_ID, NEW_PROVIDER_ID);
    }

    private void assertValidAdherence(WeeklyAdherenceSummary weeklyAdherenceSummary, String tbId, String providerId) {
        Patient patient = allPatients.findByPatientId(PatientBuilder.PATIENT_ID);
        List<Adherence> adherenceList = adherenceService.findLogsInRange(PatientBuilder.PATIENT_ID, patient.currentTherapyId(), weeklyAdherenceSummary.getWeek().startDate(), weeklyAdherenceSummary.getWeek().endDate());
        assertTbAndProviderId(adherenceList.get(0), tbId, providerId);
    }

    @Test
    // TODO : should check what current week means
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        adherenceIsRecordedForTheFirstTime();
        LocalDate startDate = allPatients.findByPatientId(PATIENT_ID).getCurrentTreatment().getTherapy().getStartDate();
        assertEquals(
                today.withDayOfWeek(DayOfWeek.Monday.getValue()).minusWeeks(1),
                startDate
        );
    }

    @Test
    public void shouldNotStartPatientOnTreatmentWhenRecordingAdherenceForAnyWeekSubsequentToFirstEverAdherenceCapturedWeek() {
        adherenceIsRecordedForTheFirstTime();
        LocalDate startDate = allPatients.findByPatientId(PATIENT_ID).getCurrentTreatment().getTherapy().getStartDate();
        assertNotNull(startDate);

        mockCurrentDate(today.plusDays(3)); //moving to the sunday -> capturing adherence for this week -> subsequent to first ever adherence captured week
        adherenceService.recordAdherence(new WeeklyAdherenceSummaryBuilder().build(), auditParams);

        assertEquals(
                startDate,
                allPatients.findByPatientId(PATIENT_ID).getCurrentTreatment().getTherapy().getStartDate()
        );
    }

    @Test
    public void shouldResetDoseStartDateWhenNoAdherenceIsPostedForSameWeek() {
        adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(1));

        adherenceService.recordAdherence(new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build(), auditParams);
        assertNull(allPatients.findByPatientId(PATIENT_ID).getCurrentTreatment().getTherapy().getStartDate());
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
    @Ignore("returns empty Weekly Adherence now")
    public void shouldReturnNullWhenCurrentWeekAdherenceIsNotCaptured() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());
        assertNull(adherenceService.currentWeekAdherence(patient));
    }

    @Test
    public void shouldLogAuditAfterRecordingAdherence() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        adherenceService.recordAdherence(new WeeklyAdherenceSummaryBuilder().build(), auditParams);
        assertEquals(1, allAuditLogs.getAll().size());
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

    private Adherence createLog(String patientId, LocalDate pillDate, PillStatus pillStatus, String tbId, String therapyDocId, String providerId) {
        Adherence log = new Adherence();
        log.setTbId(tbId);
        log.setProviderId(providerId);
        log.setPillStatus(pillStatus);
        log.setTreatmentId(therapyDocId);
        log.setPillDate(pillDate);
        log.setPatientId(patientId);
        return log;
    }

    @After
    public void tearDown() {
        super.tearDown();
        deleteAdherenceLogs();
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allAuditLogs.getAll().toArray());
        markForDeletion(allTherapies.getAll().toArray());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

    private Patient createPatient(PatientRequest request) {
        patientService.createPatient(request);
        return allPatients.findByPatientId(request.getCase_id());
    }

    private WeeklyAdherenceSummary recordAdherence() {
        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(1).forPatient(patient).build();
        adherenceService.recordAdherence(adherenceSummary, auditParams);
        return adherenceSummary;
    }

    private void adherenceIsRecordedForTheFirstTime() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withPatientType(PatientType.New)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();

        patientService.createPatient(patientRequest);
        adherenceService.recordAdherence(adherenceSummary, auditParams);
    }

    private void assertTbAndProviderId(Adherence adherence, String expectedTbId, String expectedProviderId) {
        assertEquals(expectedTbId, adherence.getTbId());
        assertEquals(expectedProviderId, adherence.getProviderId());
    }

    private void deleteAdherenceLogs() {
        for (Object log : allAdherenceLogs.getAll().toArray()) {
            adherenceDbConnector.delete(log);
        }
    }

}
