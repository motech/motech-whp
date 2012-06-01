package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AllAuditLogs;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;
import static org.motechproject.whp.adherence.util.AssertAdherence.areSame;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.*;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceIT extends SpringIntegrationTest {

    public static final String OLD_TB_ID = "elevendigit";
    public static final String OLD_PROVIDER_ID = "123456";
    public static final String PATIENT_ID = "patientid";
    public static final String NEW_PROVIDER_ID = "newproviderid";
    public static final String NEW_TB_ID = "newtbid";
    public static final String TB_ID = "elevendigit";

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
    private AllTreatments allTreatments;
    @Autowired
    private AllAuditLogs allAuditLogs;

    private final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");


    @Before
    public void setup() {
        mockCurrentDate(today);
    }

    @Test
    public void shouldRecordAdherenceForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(CASE_ID);
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().forPatient(patient).build();

        adherenceService.recordAdherence(CASE_ID, adherence, auditParams);
        assertArrayEquals(
                adherence.getAdherenceLogs().toArray(),
                adherenceService.currentWeekAdherence(patient).getAdherenceLogs().toArray()
        );
    }

    @Test
    public void shouldSetLatestTbIdAndProviderIdOnAdherenceUpdate() {
        createPatient(new PatientRequestBuilder().withDefaults().build());
        recordAdherence();
        assertTbAndProviderId(OLD_TB_ID, OLD_PROVIDER_ID);

        patientService.update(UpdateScope.closeTreatment, new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build());
        patientService.update(UpdateScope.transferIn, new PatientRequestBuilder().withMandatoryFieldsForTransferInTreatment().build());

        WeeklyAdherence updatedAdherence = new WeeklyAdherenceBuilder().withLog(DayOfWeek.Monday, PillStatus.NotTaken).forPatient(allPatients.findByPatientId(CASE_ID)).build();
        adherenceService.recordAdherence(CASE_ID, updatedAdherence, auditParams);

        assertTbAndProviderId(NEW_TB_ID, NEW_PROVIDER_ID);
    }

    @Test
    public void shouldStartPatientOnTreatmentAfterRecordingAdherenceForTheFirstTime() {
        WeeklyAdherence adherence = adherenceIsRecordedForTheFirstTime();
        assertEquals(
                adherence.firstDoseTakenOn(),
                allPatients.findByPatientId(CASE_ID).getCurrentProvidedTreatment().getTreatment().getStartDate()
        );
    }

    @Test
    public void shouldNotStartPatientOnTreatmentWhenRecordingAdherenceForAnyWeekSubsequentToFirstEverAdherenceCapturedWeek() {
        WeeklyAdherence adherence = adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(3)); //moving to the sunday -> capturing adherence for this week -> subsequent to first ever adherence captured week

        adherenceService.recordAdherence(CASE_ID, new WeeklyAdherence(), auditParams);
        assertEquals(
                adherence.firstDoseTakenOn(),
                allPatients.findByPatientId(CASE_ID).getCurrentProvidedTreatment().getTreatment().getStartDate()
        );
    }

    @Test
    public void shouldResetDoseStartDateWhenNoAdherenceIsPostedForSameWeek() {
        adherenceIsRecordedForTheFirstTime();
        mockCurrentDate(today.plusDays(1));

        adherenceService.recordAdherence(CASE_ID, new WeeklyAdherence(), auditParams);
        assertNull(allPatients.findByPatientId(CASE_ID).getCurrentProvidedTreatment().getTreatment().getStartDate());
    }

    @Test
    public void shouldReturnAdherenceWhenCurrentWeekAdherenceIsCaptured() {
        PatientRequest withDosesOnMonWedFri = new PatientRequestBuilder().withDefaults().build();
        Patient patient = createPatient(withDosesOnMonWedFri);

        WeeklyAdherence expectedAdherence = new WeeklyAdherenceBuilder()
                .withDefaultLogs()
                .forPatient(patient)
                .build();
        adherenceService.recordAdherence(withDosesOnMonWedFri.getCase_id(), expectedAdherence, auditParams);

        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(patient);
        areSame(expectedAdherence, adherence);
    }

    @Test
    public void shouldReturnNullWhenCurrentWeekAdherenceIsNotCaptured() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());
        assertNull(adherenceService.currentWeekAdherence(patient));
    }

    @Test
    public void shouldLogAuditAfterRecordingAdherence() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        adherenceService.recordAdherence(CASE_ID, new WeeklyAdherence(), auditParams);
        assertEquals(1, allAuditLogs.getAll().size());
    }

    @After
    public void tearDown() {
        super.tearDown();
        deleteAdherenceLogs();
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allAuditLogs.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

    private Patient createPatient(PatientRequest request) {
        patientService.createPatient(request);
        return allPatients.findByPatientId(request.getCase_id());
    }

    private void recordAdherence() {
        Patient patient = allPatients.findByPatientId(CASE_ID);
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withLog(DayOfWeek.Monday, PillStatus.Taken).forPatient(patient).build();
        adherenceService.recordAdherence(CASE_ID, adherence, auditParams);
    }

    private WeeklyAdherence adherenceIsRecordedForTheFirstTime() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withPatientType(PatientType.New)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();

        patientService.createPatient(patientRequest);
        adherenceService.recordAdherence(CASE_ID, adherence, auditParams);
        return adherence;
    }

    private void assertTbAndProviderId(String expectedTbId, String expectedProviderId) {
        assertEquals(expectedTbId, adherenceService.currentWeekAdherence(allPatients.findByPatientId(CASE_ID)).getAdherenceLogs().get(0).getTbId());
        assertEquals(expectedProviderId, adherenceService.currentWeekAdherence(allPatients.findByPatientId(CASE_ID)).getAdherenceLogs().get(0).getProviderId());
    }

    private void deleteAdherenceLogs() {
        for (Object log : allAdherenceLogs.getAll().toArray()) {
            adherenceDbConnector.delete(log);
        }
    }

}
