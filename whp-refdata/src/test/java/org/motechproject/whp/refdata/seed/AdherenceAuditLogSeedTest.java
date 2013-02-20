package org.motechproject.whp.refdata.seed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;
import static org.motechproject.whp.common.util.WHPDateUtil.toSqlDate;
import static org.motechproject.whp.common.util.WHPDateUtil.toSqlTimestamp;

public class AdherenceAuditLogSeedTest {

    AdherenceAuditLogSeed adherenceAuditLogSeed;
    @Mock
    AdherenceAuditService adherenceAuditService;
    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceAuditLogSeed = new AdherenceAuditLogSeed(adherenceAuditService, reportingPublisherService);
    }

    @Test
    public void shouldMapAdherenceLogSummaryToReportingRequest() {
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "raj", "tbId", now(), now(), "cmfAdmin", 1, Taken, "WEB");

        AdherenceAuditLogDTO adherenceAuditLogDTO = adherenceAuditLogSeed.mapToReportingRequest(adherenceAuditLog);

        assertEquals(adherenceAuditLog.getTbId(), adherenceAuditLogDTO.getTbId());
        assertEquals(toSqlDate(adherenceAuditLog.getDoseDate()), adherenceAuditLogDTO.getDoseDate());
        assertEquals(toSqlTimestamp(adherenceAuditLog.getCreationTime()), adherenceAuditLogDTO.getCreationTime());
        assertEquals(adherenceAuditLog.getNumberOfDosesTaken(), adherenceAuditLogDTO.getNumberOfDosesTaken());
        assertEquals(adherenceAuditLog.getPatientId(), adherenceAuditLogDTO.getPatientId());
        assertEquals(adherenceAuditLog.getPillStatus().name(), adherenceAuditLogDTO.getPillStatus());
        assertEquals(adherenceAuditLog.getProviderId(), adherenceAuditLogDTO.getProviderId());
        assertEquals(adherenceAuditLog.getSourceOfChange(), adherenceAuditLogDTO.getChannel());
        assertEquals(adherenceAuditLog.getUserId(), adherenceAuditLogDTO.getUserId());
    }

    @Test
    public void shouldReportExistingAdherenceAuditLogs() {
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "raj", "tbId", now(), now(), "cmfAdmin", 1, Taken, "WEB");

        List<AdherenceAuditLog> listOfAuditLogs = asList(adherenceAuditLog);
        List<AdherenceAuditLog> emptyList = new ArrayList<>();

        when(adherenceAuditService.allAuditLogs(1)).thenReturn(listOfAuditLogs);
        when(adherenceAuditService.allAuditLogs(2)).thenReturn(emptyList);

        adherenceAuditLogSeed.seed();

        verify(adherenceAuditService, times(2)).allAuditLogs(anyInt());
        verify(reportingPublisherService).reportAdherenceAuditLog(adherenceAuditLogSeed.mapToReportingRequest(adherenceAuditLog));
    }
}
