package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.common.domain.ProviderPatientCount;
import org.motechproject.whp.user.domain.ProviderIds;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class AdherenceLogServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;

    AdherenceLogService adherenceLogService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceLogService = new AdherenceLogService(allAdherenceLogs);
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
    public void shouldReturnProviderPatientCountWithAdherence() {
        LocalDate from = today().minusDays(1);
        LocalDate to = today();

        List<ProviderPatientCount> expectedResult = asList(new ProviderPatientCount("provider1", 3));
        when(allAdherenceLogs.findAllProviderPatientWithAdherenceCount(from, to)).thenReturn(expectedResult);

        assertEquals(expectedResult, adherenceLogService.getProviderPatientWithAdherenceCount(from, to));
        verify(allAdherenceLogs).findAllProviderPatientWithAdherenceCount(from, to);

    }
}
