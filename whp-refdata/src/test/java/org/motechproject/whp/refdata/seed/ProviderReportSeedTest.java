package org.motechproject.whp.refdata.seed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.refdata.seed.version5.ProviderReportSeed;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.mapper.ProviderReportingService;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderReportSeedTest {

    ProviderReportSeed providerReportSeed;

    @Mock
    ProviderService providerService;

    @Mock
    ProviderReportingService providerReportingService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReportSeed = new ProviderReportSeed(providerService, providerReportingService);
    }

    @Test
    public void shouldMigrateExistingProviders() {
        Provider provider1 = mock(Provider.class);
        Provider provider2 = mock(Provider.class);
        List providerList = asList(provider1, provider2);

        when(providerService.getAll()).thenReturn(providerList);

        providerReportSeed.migrateProviders();

        verify(providerService).getAll();
        verify(providerReportingService).reportProvider(provider1);
        verify(providerReportingService).reportProvider(provider2);
    }

}
