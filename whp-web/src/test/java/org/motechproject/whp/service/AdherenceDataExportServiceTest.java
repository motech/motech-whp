package org.motechproject.whp.service;


import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.uimodel.AdherenceLogSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;

public class AdherenceDataExportServiceTest extends BaseUnitTest {

    @Mock
    AdherenceAuditService adherenceAuditService;

    @Mock
    WHPAdherenceService adherenceService;

    private DateTime now = DateUtil.now();

    private AdherenceDataExportService adherenceDataExportService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockCurrentDate(now);
        adherenceDataExportService = new AdherenceDataExportService(adherenceAuditService, adherenceService);
    }

    @Test
    public void shouldMapFromAdherenceAuditLogToAdherenceSummary() {
        List<AdherenceAuditLog> adherenceAuditLogList = new ArrayList<>();
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "raj", "tbId", now, now, "cmfAdmin", 1, Taken, "WEB");
        adherenceAuditLogList.add(adherenceAuditLog);

        AdherenceLogSummary expectedAdherenceLogSummary = new AdherenceLogSummary("patient1", "tbId", this.now.toDate(), this.now.toDate(), "cmfAdmin", 1, Taken, "WEB", "raj");

        List<AdherenceLogSummary> adherenceLogSummaryList = adherenceDataExportService.map(adherenceAuditLogList);
        assertThat(adherenceLogSummaryList.get(0), Is.is(expectedAdherenceLogSummary));
    }

    @Test
    public void shouldMapFromAdherenceAuditLogToAdherenceSummaryForNullDoseDate() {
        List<AdherenceAuditLog> adherenceAuditLogList = new ArrayList<>();
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "raj", "tbId", now, null, "cmfAdmin", 1, Taken, "WEB");
        adherenceAuditLogList.add(adherenceAuditLog);

        AdherenceLogSummary expectedAdherenceLogSummary = new AdherenceLogSummary("patient1", "tbId", this.now.toDate(), null, "cmfAdmin", 1, Taken, "WEB", "raj");

        List<AdherenceLogSummary> adherenceLogSummaryList = adherenceDataExportService.map(adherenceAuditLogList);
        assertThat(adherenceLogSummaryList.get(0), Is.is(expectedAdherenceLogSummary));
    }

    @Test
    public void shouldPageAuditLogSummaries() {
        int pageNo = 1;
        List<AdherenceAuditLog> auditLogs = Arrays.asList(new AdherenceAuditLog("patient1", "raj", "tbId", DateUtil.now(), DateUtil.now(), "cmfAdmin", 1, PillStatus.Taken, "WEB"));
        when(adherenceAuditService.allAuditLogs(1)).thenReturn(auditLogs);

        List<AdherenceLogSummary> adherenceAuditLogSummaries = adherenceDataExportService.adherenceAuditReport(pageNo);

        List<AdherenceLogSummary> expectedAuditLogSummaries = Arrays.asList(new AdherenceLogSummary("patient1", "tbId", this.now.toDate(), this.now.toDate(), "cmfAdmin", 1, Taken, "WEB", "raj"));

        assertEquals(expectedAuditLogSummaries, adherenceAuditLogSummaries);
        verify(adherenceAuditService).allAuditLogs(pageNo);
    }
}
