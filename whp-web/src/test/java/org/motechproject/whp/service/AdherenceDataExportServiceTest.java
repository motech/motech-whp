package org.motechproject.whp.service;


import org.hamcrest.core.Is;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.uimodel.AdherenceLogSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.joda.time.LocalDate.now;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.newDateTime;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;

public class AdherenceDataExportServiceTest {

    @Mock
    AdherenceAuditService adherenceAuditService;

    private AdherenceDataExportService adherenceDataExportService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        adherenceDataExportService = new AdherenceDataExportService(adherenceAuditService);
    }

    @Test
    public void shouldMapFromAdherenceAuditLogToAdherenceSummary() {
        LocalDate now = now();
        List<AdherenceAuditLog> adherenceAuditLogList = new ArrayList<>();
        AdherenceAuditLog adherenceAuditLog = new AdherenceAuditLog("patient1", "tbId", newDateTime(now), newDateTime(now), "cmfAdmin", 1, Taken, "WEB");
        adherenceAuditLogList.add(adherenceAuditLog);

        AdherenceLogSummary expectedAdherenceLogSummary = new AdherenceLogSummary("patient1", "tbId", new WHPDate(now()).value(), new WHPDate(now()).value(), "cmfAdmin", 1, Taken, "WEB");

        List<AdherenceLogSummary> adherenceLogSummaryList = adherenceDataExportService.map(adherenceAuditLogList);
        assertThat(adherenceLogSummaryList.get(0), Is.is(expectedAdherenceLogSummary));
    }

    @Test
    public void shouldPageAuditLogSummaries(){
        int pageNo = 1;
        List<AdherenceAuditLog> auditLogs = Arrays.asList(new AdherenceAuditLog("patient1", "tbId", DateUtil.now(), DateUtil.now(), "cmfAdmin", 1, PillStatus.Taken, "WEB"));
        when(adherenceAuditService.allAuditLogs(1)).thenReturn(auditLogs);

        List<AdherenceLogSummary> adherenceAuditLogSummaries = adherenceDataExportService.adherenceReport(pageNo);

        List<AdherenceLogSummary> expectedAuditLogSummaries = Arrays.asList(new AdherenceLogSummary("patient1", "tbId", new WHPDate(now()).value(), new WHPDate(now()).value(), "cmfAdmin", 1, Taken, "WEB"));

        assertEquals(expectedAuditLogSummaries, adherenceAuditLogSummaries);
        verify(adherenceAuditService).allAuditLogs(pageNo);
    }
}
