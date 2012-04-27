package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.request.ProviderWebRequest;

import static junit.framework.Assert.assertEquals;


public class ProviderRequestMapperTest {

    ProviderRequestMapper providerRequestMapper;

    @Before
    public void setUp() {
        providerRequestMapper = new ProviderRequestMapper();
    }

    @Test
    public void shouldCreatePatient() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withDefaults().build();

        ProviderRequest provider = providerRequestMapper.map(providerWebRequest);

        assertEquals("providerId", provider.getProviderId());
        assertEquals("9880123456", provider.getPrimaryMobile());
        assertEquals("9880123457", provider.getSecondaryMobile());
        assertEquals("9880123458", provider.getTertiaryMobile());
        assertEquals("Patna", provider.getDistrict());
        assertEquals("12/01/2012 10:10:10", provider.getLastModifiedDate().toString("dd/MM/YYYY HH:mm:ss"));
    }
}

