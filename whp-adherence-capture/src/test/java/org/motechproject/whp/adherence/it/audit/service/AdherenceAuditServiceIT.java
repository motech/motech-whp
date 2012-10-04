package org.motechproject.whp.adherence.it.audit.service;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.domain.WeeklyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.builder.AdherenceDataBuilder;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;
import static org.motechproject.whp.patient.builder.PatientBuilder.TB_ID;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
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

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals(3, weeklyAdherenceAuditLog.numberOfDosesTaken());
        assertEquals("remarks", weeklyAdherenceAuditLog.remark());
        assertEquals("WEB", weeklyAdherenceAuditLog.sourceOfChange());
        assertEquals(PATIENT_ID, weeklyAdherenceAuditLog.patientId());
        assertEquals(TB_ID, weeklyAdherenceAuditLog.tbId());
        assertEquals("user", weeklyAdherenceAuditLog.user());
    }

    @Test
    public void shouldTrimRemarkOnStoringAuditLog() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        auditParams.setRemarks("   remarks  ");
        adherenceAuditService.auditWeeklyAdherence(patient, adherenceSummary, auditParams);

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals("remarks", weeklyAdherenceAuditLog.remark());
    }

    @Test
    public void shouldHandleNullRemarkValue() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        auditParams.setRemarks(null);
        adherenceAuditService.auditWeeklyAdherence(patient, adherenceSummary, auditParams);

        WeeklyAdherenceAuditLog weeklyAdherenceAuditLog = adherenceAuditService.fetchWeeklyAuditLogs().get(0);

        assertEquals(null, weeklyAdherenceAuditLog.remark());
    }

    @Test
    public void shouldAuditDailyAdherenceCaptured(){
        LocalDate pillDate = DateUtil.today();
        Adherence adherenceData = AdherenceDataBuilder.createLog(pillDate, "providerId", TB_ID, PillStatus.Taken);

        adherenceAuditService.auditDailyAdherence(patient, Arrays.asList(adherenceData), auditParams);

        DailyAdherenceAuditLog auditLog = adherenceAuditService.fetchDailyAuditLogs().get(0);

        assertEquals(PATIENT_ID, auditLog.getPatientId());
        assertEquals(TB_ID, auditLog.getTbId());
        assertEquals(pillDate, auditLog.getPillDate());
        assertEquals(PillStatus.Taken, auditLog.getPillStatus());
        assertEquals("user", auditLog.getUser());
        assertEquals("WEB", auditLog.getSourceOfChange());

    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceWeeklyAdherenceAuditLogs.getAll().toArray());
        markForDeletion(allDailyAdherenceAuditLogs.getAll().toArray());
    }
}
