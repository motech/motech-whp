package org.motechproject.whp.user.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.motechproject.whp.user.domain.Provider;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderReportingServiceTest {

    ProviderReportingService providerReportingService;

    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        providerReportingService = new ProviderReportingService(reportingPublisherService);
    }

    @Test
    public void shouldReportGivenProvider() {
        Provider provider = new Provider();
        provider.setProviderId("providerId");
        provider.setDistrict("district");
        provider.setPrimaryMobile("primary");
        provider.setSecondaryMobile("secondary");
        provider.setTertiaryMobile("tertiary");

        ProviderDTO expectedProviderDTO = new ProviderDTO();
        expectedProviderDTO.setProviderId(provider.getProviderId());
        expectedProviderDTO.setDistrict(provider.getDistrict());
        expectedProviderDTO.setPrimaryMobile(provider.getPrimaryMobile());
        expectedProviderDTO.setSecondaryMobile(provider.getSecondaryMobile());
        expectedProviderDTO.setTertiaryMobile(provider.getTertiaryMobile());

        providerReportingService.reportProvider(provider);

        verify(reportingPublisherService).reportProvider(expectedProviderDTO);
    }

}
