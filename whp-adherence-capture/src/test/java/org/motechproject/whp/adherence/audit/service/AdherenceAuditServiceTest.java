package org.motechproject.whp.adherence.audit.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.adherence.audit.reporting.AdherenceAuditLogReportingService;
import org.motechproject.whp.adherence.audit.repository.AllAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceAuditServiceTest extends BaseUnitTest {
    @Mock
    AllAdherenceAuditLogs allAdherenceAuditLogs;
    @Mock
    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;
    @Mock
    AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;

    @Mock
    AdherenceAuditLogReportingService adherenceAuditLogReportingService;

    private AdherenceAuditService adherenceAuditService;

    @Mock
    Patient patient;

    @Mock
    AuditParams auditParams;

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(DateUtil.now());
        adherenceAuditService = new AdherenceAuditService(allWeeklyAdherenceAuditLogs, allDailyAdherenceAuditLogs, allAdherenceAuditLogs, adherenceAuditLogReportingService);
    }

    @Test
    public void shouldFetchAllAuditLogs() {
        DateTime now = DateUtil.now();
        int pageSize = 10000;
        int pageNumber = 1;

        List<AdherenceAuditLog> expectedAuditLogs = Arrays.asList(new AdherenceAuditLog("patient1", "providerId", "tbId", now, now, "cmfAdmin", 1, PillStatus.Taken, "WEB"));
        when(allAdherenceAuditLogs.findLogsAsOf(now.minusMonths(3), now, pageNumber - 1, pageSize)).thenReturn(expectedAuditLogs);

        List<AdherenceAuditLog> auditLogs = adherenceAuditService.allAuditLogs(pageNumber);

        assertEquals(expectedAuditLogs, auditLogs);
        verify(allAdherenceAuditLogs).findLogsAsOf(now.minusMonths(3), now, pageNumber - 1, pageSize);

    }

    @Test
    public void shouldReportWeeklyAdherenceAuditLogs() {
        DateTime now = DateUtil.now();
        AuditLog auditLog = new AuditLog(now, 2, "remarks", "IVR", "patientId", "tbId", "providerId", "user");

        WeeklyAdherenceSummary weeklyAdherenceSummary = mock(WeeklyAdherenceSummary.class);
        Therapy therapy = mock(Therapy.class);
        Treatment currentTreatment = mock(Treatment.class);

        when(patient.getCurrentTherapy()).thenReturn(therapy);
        when(therapy.getCurrentTreatment()).thenReturn(currentTreatment);

        when(weeklyAdherenceSummary.getDosesTaken()).thenReturn(2);
                when(currentTreatment.getProviderId()).thenReturn("providerId");
                when(auditParams.getRemarks()).thenReturn("remarks");
                when(auditParams.getUser()).thenReturn("user");
                when(auditParams.getSourceOfChange()).thenReturn(AdherenceSource.IVR);
                when(weeklyAdherenceSummary.getPatientId()).thenReturn("patientId");
                when(currentTreatment.getTbId()).thenReturn("tbId");

        adherenceAuditService.auditWeeklyAdherence(patient, weeklyAdherenceSummary, auditParams);

        verify(adherenceAuditLogReportingService).reportAuditLog(auditLog);
    }

    @Test
    public void shouldReportDailyAdherenceAuditLogs() {
        DateTime now = DateUtil.now();

        DailyAdherenceAuditLog dailyAdherenceAuditLog = new DailyAdherenceAuditLog("patientId", "tbId",now.toLocalDate(), PillStatus.Taken,"user","IVR", now ,"providerId");

        Adherence adherence = mock(Adherence.class);

        when(patient.getPatientId()).thenReturn("patientId");
        when(adherence.getTbId()).thenReturn("tbId");
        when(adherence.getPillDate()).thenReturn(now.toLocalDate());
        when(adherence.getPillStatus()).thenReturn(PillStatus.Taken);
        when(auditParams.getSourceOfChange()).thenReturn(AdherenceSource.IVR);
        when(auditParams.getUser()).thenReturn("user");
        when(adherence.getProviderId()).thenReturn("providerId");

        adherenceAuditService.auditDailyAdherence(patient, asList(adherence), auditParams);

        verify(adherenceAuditLogReportingService).reportDailyAdherenceAuditLog(dailyAdherenceAuditLog);
    }
}