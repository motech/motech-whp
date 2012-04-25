package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.domain.Provider;
import org.motechproject.whp.request.ProviderRequest;

import static junit.framework.Assert.assertEquals;


public class ProviderMapperTest {

    ProviderMapper providerMapper;

    @Before
    public void setUp() {
        providerMapper = new ProviderMapper();
    }

    @Test
    public void shouldCreatePatient() {
        ProviderRequest providerRequest = new ProviderRequestBuilder().withDefaults().build();

        Provider provider = providerMapper.map(providerRequest);

        assertEquals("providerId", provider.getProviderId());
        assertEquals("9880123456", provider.getPrimaryMobile());
        assertEquals("9880123457", provider.getSecondaryMobile());
        assertEquals("9880123458", provider.getTertiaryMobile());
        assertEquals("Patna", provider.getDistrict());
        assertEquals("12/01/2012 10:10:10", provider.getLastModifiedDate().toString("dd/MM/YYYY HH:mm:ss"));
    }
}

