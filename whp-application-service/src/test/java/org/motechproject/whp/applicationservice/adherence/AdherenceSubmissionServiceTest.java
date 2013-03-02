package org.motechproject.whp.applicationservice.adherence;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class AdherenceSubmissionServiceTest {

    private AdherenceSubmissionService adherenceSubmissionService;

    @Mock
    private ProviderService providerService;

    @Mock
    private AdherenceLogService adherenceLogService;

    @Mock
    private PatientService patientService;

    @Before
    public void setup() {
        initMocks(this);
        adherenceSubmissionService = new AdherenceSubmissionService(providerService, patientService);
    }

    @Test
    public void shouldReturnListOfAllProvidersWithPendingAdherence() {
        LocalDate asOfDate = today();
        ProviderIds providerIds = new ProviderIds(asList("provider1"));
        List<Provider> expectedProviderList = asList(new Provider("provider1", "msisdn", "district", now()));

        when(patientService.getAllProvidersWithPendingAdherence(asOfDate)).thenReturn(providerIds);
        when(providerService.findByProviderIds(providerIds)).thenReturn(expectedProviderList);

        List<Provider> providersPendingAdherence = adherenceSubmissionService.providersPendingAdherence(asOfDate);

        assertEquals(expectedProviderList, providersPendingAdherence);
        verify(patientService).getAllProvidersWithPendingAdherence(asOfDate);
        verify(providerService).findByProviderIds(providerIds);
    }
}
