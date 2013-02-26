package org.motechproject.whp.refdata.seed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.adherence.service.AdherenceRecordReportingService;
import org.motechproject.whp.refdata.seed.version5.AdherenceRecordSeed;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceRecordSeedTest {
    @Mock
    AdherenceLogService adherenceLogService;
    @Mock
    AdherenceRecordReportingService adherenceRecordReportingService;

    @Before
    public void setUp(){
        initMocks(this);
    }

    @Test
    public void shouldSeedAdherenceRecords() {
        AdherenceRecord adherenceRecord = mock(AdherenceRecord.class);
        List<AdherenceRecord>  adherenceRecords = asList(adherenceRecord);
        List<AdherenceRecord>  emptyList = asList();

        when(adherenceLogService.fetchAllAdherenceRecords(1)).thenReturn(adherenceRecords);
        when(adherenceLogService.fetchAllAdherenceRecords(2)).thenReturn(emptyList);

        AdherenceRecordSeed adherenceRecordSeed = new AdherenceRecordSeed(adherenceLogService, adherenceRecordReportingService);
        adherenceRecordSeed.reportAdherenceRecords();
        verify(adherenceLogService).fetchAllAdherenceRecords(1);
        verify(adherenceLogService).fetchAllAdherenceRecords(2);
        verify(adherenceRecordReportingService).report(adherenceRecord);

    }
}
