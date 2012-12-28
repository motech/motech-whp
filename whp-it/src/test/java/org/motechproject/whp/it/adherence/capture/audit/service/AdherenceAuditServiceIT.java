package org.motechproject.whp.it.adherence.capture.audit.service;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.builder.AdherenceDataBuilder;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.it.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AdherenceAuditServiceIT extends SpringIntegrationTest {

    Patient patient;

    @Autowired
    private AllWeeklyAdherenceAuditLogs allAdherenceWeeklyAdherenceAuditLogs;
    @Autowired
    private AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;

    @Autowired
    private AdherenceAuditService adherenceAuditService;
    private final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");
    
    @Before
    public void setUp() {
        patient = new PatientBuilder().withDefaults().build();
    }

    @Test
    public void shouldAuditWeeklyAdherenceCaptured() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceAuditService.auditWeeklyAdherence(patient, adherenceSummary, auditParams);

        AuditLog auditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals(3, auditLog.numberOfDosesTaken());
        assertEquals("remarks", auditLog.remark());
        assertEquals("WEB", auditLog.sourceOfChange());
        assertEquals(PatientBuilder.PATIENT_ID, auditLog.patientId());
        assertEquals(PatientBuilder.TB_ID, auditLog.tbId());
        assertEquals("user", auditLog.user());
    }

    @Test
    public void shouldTrimRemarkOnStoringAuditLog() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        auditParams.setRemarks("   remarks  ");
        adherenceAuditService.auditWeeklyAdherence(patient, adherenceSummary, auditParams);

        AuditLog auditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals("remarks", auditLog.remark());
    }

    @Test
    public void shouldHandleNullRemarkValue() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        auditParams.setRemarks(null);
        adherenceAuditService.auditWeeklyAdherence(patient, adherenceSummary, auditParams);

        AuditLog auditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals(null, auditLog.remark());
    }

    @Test
    public void shouldAuditDailyAdherenceCaptured(){
        LocalDate pillDate = DateUtil.today();
        String providerId = "providerId";
        Adherence adherenceData = AdherenceDataBuilder.createLog(pillDate, providerId, PatientBuilder.TB_ID, PillStatus.Taken);

        adherenceAuditService.auditDailyAdherence(patient, Arrays.asList(adherenceData), auditParams);

        DailyAdherenceAuditLog auditLog = adherenceAuditService.fetchDailyAuditLogs().get(0);

        assertEquals(PatientBuilder.PATIENT_ID, auditLog.getPatientId());
        assertEquals(PatientBuilder.TB_ID, auditLog.getTbId());
        assertEquals(pillDate, auditLog.getPillDate());
        assertEquals(PillStatus.Taken, auditLog.getPillStatus());
        assertEquals("user", auditLog.getUser());
        assertEquals("WEB", auditLog.getSourceOfChange());
        assertEquals(providerId, auditLog.getProviderId());

    }

    @After
    public void tearDown() {
        allAdherenceWeeklyAdherenceAuditLogs.removeAll();
        allDailyAdherenceAuditLogs.removeAll();
    }
}
