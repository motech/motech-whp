package org.motechproject.whp.adherence.audit.reporting;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceAuditLogReportingServiceTest {

    AdherenceAuditLogReportingService adherenceAuditLogReportingService;

    @Mock
    ReportingPublisherService reportingPublisherService;

    @Mock
    AdherenceAuditLogMapper adherenceAuditLogMapper;

    @Mock
    AdherenceAuditLogDTO adherenceAuditLogDTO;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        adherenceAuditLogReportingService = new AdherenceAuditLogReportingService(reportingPublisherService, adherenceAuditLogMapper);
    }

    @Test
    public void shouldReportAdherenceAuditLog() {
        AuditLog auditLog = new AuditLog();

        when(adherenceAuditLogMapper.mapFromAuditLog(auditLog)).thenReturn(adherenceAuditLogDTO);

        adherenceAuditLogReportingService.reportAuditLog(auditLog);

        verify(adherenceAuditLogMapper).mapFromAuditLog(auditLog);
        verify(reportingPublisherService).reportAdherenceAuditLog(adherenceAuditLogDTO);
    }

    @Test
    public void shouldReportDailyAdherenceAuditLog() {
        DailyAdherenceAuditLog dailyAdherenceAuditLog = new DailyAdherenceAuditLog();

        when(adherenceAuditLogMapper.mapFromDailyAdherenceAuditLog(dailyAdherenceAuditLog)).thenReturn(adherenceAuditLogDTO);

        adherenceAuditLogReportingService.reportDailyAdherenceAuditLog(dailyAdherenceAuditLog);

        verify(adherenceAuditLogMapper).mapFromDailyAdherenceAuditLog(dailyAdherenceAuditLog);
        verify(reportingPublisherService).reportAdherenceAuditLog(adherenceAuditLogDTO);
    }
}
