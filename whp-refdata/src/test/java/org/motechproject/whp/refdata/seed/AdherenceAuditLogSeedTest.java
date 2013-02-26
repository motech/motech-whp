package org.motechproject.whp.refdata.seed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceAuditLogDTO;
import org.motechproject.whp.reports.contract.enums.YesNo;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
        AdherenceAuditLog adherenceAuditLogByCmfAdmin = new AdherenceAuditLog("patient1", "raj", "tbId", now(), now(), "cmfAdmin", null, Taken, "WEB");
        AdherenceAuditLog adherenceAuditLogByProvider = new AdherenceAuditLog("patient1", "raj", "tbId", now(), now(), "raj", 1, Taken, "WEB");

        AdherenceAuditLogDTO adherenceAuditLogDTO1 = adherenceAuditLogSeed.mapToReportingRequest(adherenceAuditLogByCmfAdmin);
        AdherenceAuditLogDTO adherenceAuditLogDTO2 = adherenceAuditLogSeed.mapToReportingRequest(adherenceAuditLogByProvider);

        assertEquals(adherenceAuditLogByCmfAdmin.getTbId(), adherenceAuditLogDTO1.getTbId());
        assertEquals(toSqlDate(adherenceAuditLogByCmfAdmin.getDoseDate()), adherenceAuditLogDTO1.getDoseDate());
        assertEquals(toSqlTimestamp(adherenceAuditLogByCmfAdmin.getCreationTime()), adherenceAuditLogDTO1.getCreationTime());
        assertEquals(adherenceAuditLogByCmfAdmin.getNumberOfDosesTaken(), adherenceAuditLogDTO1.getNumberOfDosesTaken());
        assertEquals(adherenceAuditLogByCmfAdmin.getPatientId(), adherenceAuditLogDTO1.getPatientId());
        assertEquals(adherenceAuditLogByCmfAdmin.getPillStatus().name(), adherenceAuditLogDTO1.getPillStatus());
        assertEquals(adherenceAuditLogByCmfAdmin.getProviderId(), adherenceAuditLogDTO1.getProviderId());
        assertEquals(adherenceAuditLogByCmfAdmin.getSourceOfChange(), adherenceAuditLogDTO1.getChannel());
        assertEquals(adherenceAuditLogByCmfAdmin.getUserId(), adherenceAuditLogDTO1.getUserId());
        assertThat(adherenceAuditLogDTO1.getIsGivenByProvider(), is(YesNo.No.name()));
        assertThat(adherenceAuditLogDTO2.getIsGivenByProvider(), is(YesNo.Yes.name()));
    }

    @Test
    public void shouldReportExistingAdherenceAuditLogs() {
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "raj", "tbId", now(), now(), "cmfAdmin", 1, Taken, "WEB");

        List<AdherenceAuditLog> listOfAuditLogs = asList(adherenceAuditLog);
        List<AdherenceAuditLog> emptyList = new ArrayList<>();

        when(adherenceAuditService.fetchAllAuditLogs(1)).thenReturn(listOfAuditLogs);
        when(adherenceAuditService.fetchAllAuditLogs(2)).thenReturn(emptyList);

        adherenceAuditLogSeed.seed();

        verify(adherenceAuditService, times(2)).fetchAllAuditLogs(anyInt());
        verify(reportingPublisherService).reportAdherenceAuditLog(adherenceAuditLogSeed.mapToReportingRequest(adherenceAuditLog));
    }
}
