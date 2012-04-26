package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.contract.CreateProviderRequest;
import org.motechproject.whp.request.ProviderRequest;

import static junit.framework.Assert.assertEquals;


public class CreateProviderRequestMapperTest {

    CreateProviderRequestMapper createProviderRequestMapper;

    @Before
    public void setUp() {
        createProviderRequestMapper = new CreateProviderRequestMapper();
    }

    @Test
    public void shouldCreatePatient() {
        ProviderRequest providerRequest = new ProviderRequestBuilder().withDefaults().build();

        CreateProviderRequest provider = createProviderRequestMapper.map(providerRequest);

        assertEquals("providerId", provider.getProviderId());
        assertEquals("9880123456", provider.getPrimaryMobile());
        assertEquals("9880123457", provider.getSecondaryMobile());
        assertEquals("9880123458", provider.getTertiaryMobile());
        assertEquals("Patna", provider.getDistrict());
        assertEquals("12/01/2012 10:10:10", provider.getLastModifiedDate().toString("dd/MM/YYYY HH:mm:ss"));
    }
}

