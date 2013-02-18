package org.motechproject.whp.user.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.motechproject.whp.user.domain.Provider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProviderReportingRequestMapperTest {

    ProviderReportingRequestMapper providerReportingRequestMapper;

    @Before
    public void setUp() throws Exception {
        providerReportingRequestMapper = new ProviderReportingRequestMapper();
    }

    @Test
    public void shouldMapProviderToProviderDTO() {
        Provider provider = new Provider();
        provider.setProviderId("providerId");
        provider.setDistrict("district");
        provider.setPrimaryMobile("primary");
        provider.setSecondaryMobile("secondary");
        provider.setTertiaryMobile("tertiary");

        ProviderDTO actualProviderDTO = providerReportingRequestMapper.map(provider);
        assertThat(actualProviderDTO.getProviderId(), is(provider.getProviderId()));
        assertThat(actualProviderDTO.getDistrict(), is(provider.getDistrict()));
        assertThat(actualProviderDTO.getPrimaryMobile(), is(provider.getPrimaryMobile()));
        assertThat(actualProviderDTO.getSecondaryMobile(), is(provider.getSecondaryMobile()));
        assertThat(actualProviderDTO.getTertiaryMobile(), is(provider.getTertiaryMobile()));
    }
}
