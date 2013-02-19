package org.motechproject.whp.adherence.audit.reporting;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
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
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog();

        when(adherenceAuditLogMapper.map(adherenceAuditLog)).thenReturn(adherenceAuditLogDTO);

        adherenceAuditLogReportingService.reportAdherenceAuditLog(adherenceAuditLog);

        verify(adherenceAuditLogMapper).map(adherenceAuditLog);
        verify(reportingPublisherService).reportAdherenceAuditLog(adherenceAuditLogDTO);
    }
}
