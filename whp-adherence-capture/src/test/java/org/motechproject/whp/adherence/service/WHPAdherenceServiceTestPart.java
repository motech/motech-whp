package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.repository.AllAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public abstract class WHPAdherenceServiceTestPart extends SpringIntegrationTest {

    LocalDate today = DateUtil.newDate(2012, 5, 3);

    @Autowired
    @Qualifier(value = "whpDbConnector")
    CouchDbConnector couchDbConnector;

    @Autowired
    @Qualifier(value = "adherenceDbConnector")
    CouchDbConnector adherenceDbConnector;
    @Autowired
    @Qualifier(value = "whpDbConnector")
    CouchDbConnector dailyAdherenceDbConnector;
    @Autowired
    WHPAdherenceService adherenceService;

    @Autowired
    PatientService patientService;
    @Autowired
    AllAdherenceLogs allAdherenceLogs;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllAuditLogs allAuditLogs;

    @Autowired
    AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;
    final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");
    final String THERAPY_DOC_ID = "THERAPY_DOC_ID";

    @Before
    public void setup() {
        mockCurrentDate(today);
    }

    @After
    public void tearDown() {
        super.tearDown();
        deleteAdherenceLogs();
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allAuditLogs.getAll().toArray());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }

    private void deleteAdherenceLogs() {
        for (Object log : allAdherenceLogs.getAll().toArray()) {
            adherenceDbConnector.delete(log);
        }

        for (Object log : allDailyAdherenceAuditLogs.getAll().toArray())
            dailyAdherenceDbConnector.delete(log);
    }

    protected Adherence createLog(String patientId, LocalDate pillDate, PillStatus pillStatus, String tbId, String therapyUid, String providerId) {
        Adherence log = new Adherence();
        log.setTbId(tbId);
        log.setProviderId(providerId);
        log.setPillStatus(pillStatus);
        log.setTreatmentId(therapyUid);
        log.setPillDate(pillDate);
        log.setPatientId(patientId);
        return log;
    }

    protected Patient createPatient(PatientRequest request) {
        patientService.createPatient(request);
        return allPatients.findByPatientId(request.getCase_id());
    }

    protected WeeklyAdherenceSummary recordAdherence() {
        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(1).forPatient(patient).build();
        adherenceService.recordWeeklyAdherence(adherenceSummary, auditParams);
        return adherenceSummary;
    }

    protected void adherenceIsRecordedForTheFirstTime() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withPatientType(PatientType.New)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();

        patientService.createPatient(patientRequest);
        adherenceService.recordWeeklyAdherence(adherenceSummary, auditParams);
    }

    protected void assertTbAndProviderId(Adherence adherence, String expectedTbId, String expectedProviderId) {
        assertEquals(expectedTbId, adherence.getTbId());
        assertEquals(expectedProviderId, adherence.getProviderId());
    }
}
