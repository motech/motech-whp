package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.user.domain.ProviderIds;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class AdherenceLogServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;
    @Mock
    AdherenceRecordReportingService adherenceRecordReportingService;

    AdherenceLogService adherenceLogService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceLogService = new AdherenceLogService(allAdherenceLogs, adherenceRecordReportingService);
    }

    @Test
    public void shouldCountDosesTakenBetweenTwoDates() {
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();
        String patientId = "patientId";
        String treatmentId = "treatmentId";

        adherenceLogService.countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);

        verify(allAdherenceLogs).countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);
    }

    @Test
    public void shouldReturnProvidersWithAdherenceRecordsInADistrict() {
        String district = "district";
        ProviderIds providersWithAdherenceRecords = new ProviderIds(asList("providerId1"));
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();

        when(allAdherenceLogs.withKnownAdherenceReportedByProviders(district, yesterday, today)).thenReturn(providersWithAdherenceRecords);
        assertEquals(providersWithAdherenceRecords, adherenceLogService.providersWithAdherence(district, yesterday, today));
    }

    @Test
    public void shouldReturnProvidersWithAdherence() {
        ProviderIds providersWithAdherenceRecords = new ProviderIds(asList("providerId1"));
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();

        when(allAdherenceLogs.findProvidersWithAdherence(yesterday, today)).thenReturn(providersWithAdherenceRecords);
        assertEquals(providersWithAdherenceRecords, adherenceLogService.providersWithAdherence(yesterday, today));
    }

    @Test
    public void shouldSaveAndReportAdherenceRecords() {
        AdherenceRecord adherenceRecord = mock(AdherenceRecord.class);
        List<AdherenceRecord> adherenceRecords = asList(adherenceRecord);

        adherenceLogService.saveOrUpdateAdherence(adherenceRecords);

        verify(adherenceRecordReportingService).report(adherenceRecord);
        verify(allAdherenceLogs).add(any(AdherenceLog.class));
    }

    @Test
    public void shouldFetchAllAuditLogs() {
        int pageSize = 10000;
        int pageNumber = 1;

        List<AdherenceRecord> expectedAdherenceRecords = mock(List.class);
        when(allAdherenceLogs.allLogs(pageNumber - 1, pageSize)).thenReturn(expectedAdherenceRecords);

        List<AdherenceRecord> auditLogs = adherenceLogService.fetchAllAdherenceRecords(pageNumber);

        Assert.assertEquals(expectedAdherenceRecords, auditLogs);
        verify(allAdherenceLogs).allLogs(pageNumber - 1, pageSize);
    }

}
