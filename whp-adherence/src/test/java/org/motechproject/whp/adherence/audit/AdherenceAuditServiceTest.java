package org.motechproject.whp.adherence.audit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;
import static org.motechproject.whp.patient.builder.PatientBuilder.TB_ID;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AdherenceAuditServiceTest extends SpringIntegrationTest {

    Patient patient;

    @Autowired
    private AllAuditLogs allAdherenceAuditLogs;

    @Autowired
    private AdherenceAuditService adherenceAuditService;
    private final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");
    
    @Before
    public void setUp() {
        patient = new PatientBuilder().withDefaults().build();
    }

    @Test
    public void shouldLogNumberOfDosesTakenWhenAdherenceCaptured() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals(3, adherenceAuditService.fetchAuditLogs().get(0).numberOfDosesTaken());
    }

    @Test
    public void shouldLogRemarksWhenAdherenceCaptured() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals("remarks", adherenceAuditService.fetchAuditLogs().get(0).remark());
    }

    @Test
    public void shouldLogSourceOfChangeWhenAdherenceCaptured() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals("WEB", adherenceAuditService.fetchAuditLogs().get(0).sourceOfChange());
    }

    @Test
    public void shouldLogPatientIdWhenAdherenceCaptured() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(3).build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals(PATIENT_ID, adherenceAuditService.fetchAuditLogs().get(0).patientId());
    }

    @Test
    public void shouldLogTbIdWhenAdherenceCaptured() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals(TB_ID, adherenceAuditService.fetchAuditLogs().get(0).tbId());
    }

    @Test
    public void shouldLogRemarksWhenAdherenceIsCaptured(){
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals("remarks", adherenceAuditService.fetchAuditLogs().get(0).remark());
    }

    @Test
    public void shouldLogUserWhenAdherenceIsCaptured(){
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.log(patient, adherenceSummary, auditParams);
        assertEquals("user", adherenceAuditService.fetchAuditLogs().get(0).user());
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceAuditLogs.getAll().toArray());
    }
}
