package org.motechproject.whp.adherence.audit.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.repository.AllAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllDailyAdherenceAuditLogs;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceAuditServiceTest extends BaseUnitTest {
    @Mock
    AllAdherenceAuditLogs allAdherenceAuditLogs;
    @Mock
    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;
    @Mock
    AllDailyAdherenceAuditLogs allDailyAdherenceAuditLogs;
    private AdherenceAuditService adherenceAuditService;

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(DateUtil.now());
        adherenceAuditService = new AdherenceAuditService(allWeeklyAdherenceAuditLogs, allDailyAdherenceAuditLogs, allAdherenceAuditLogs);
    }

    @Test
    public void shouldFetchAllAuditLogs() {
        DateTime now = DateUtil.now();
        int pageSize = 10000;
        int pageNumber = 1;

        List<AdherenceAuditLog> expectedAuditLogs = Arrays.asList(new AdherenceAuditLog("patient1", "tbId", now, now, "cmfAdmin", 1, PillStatus.Taken, "WEB"));
        when(allAdherenceAuditLogs.findLogsAsOf(now, pageNumber - 1, pageSize)).thenReturn(expectedAuditLogs);

        List<AdherenceAuditLog> auditLogs = adherenceAuditService.allAuditLogs(pageNumber);

        assertEquals(expectedAuditLogs, auditLogs);
        verify(allAdherenceAuditLogs).findLogsAsOf(now, pageNumber - 1, pageSize);
    }

}
