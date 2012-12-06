package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.user.domain.ProviderIds;

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
    public void shouldReturnProvidersWithAdherenceRecords() {
        ProviderIds providersToSearchFor = new ProviderIds(asList("providerId1", "providerId2"));
        ProviderIds providersWithAdherenceRecords = new ProviderIds(asList("providerId1"));
        LocalDate yesterday = today().minusDays(1);
        LocalDate today = today();

        when(allAdherenceLogs.withKnownAdherenceReportedByProviders(providersToSearchFor, yesterday, today)).thenReturn(providersWithAdherenceRecords);
        assertEquals(providersWithAdherenceRecords, adherenceLogService.providersWithAdherenceRecords(providersToSearchFor, yesterday, today));
    }
}
