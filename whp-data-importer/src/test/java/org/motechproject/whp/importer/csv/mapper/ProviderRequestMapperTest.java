package org.motechproject.whp.importer.csv.mapper;

import org.junit.Test;
import org.motechproject.whp.importer.csv.builder.ImportProviderRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.contract.ProviderRequest;

import static org.junit.Assert.assertEquals;

public class ProviderRequestMapperTest{
    @Test
    public void shouldMapImportProviderRequestToProviderRequest() throws Exception {
        ProviderRequestMapper providerRequestMapper = new ProviderRequestMapper();
        String providerId = "12";
        String district = "distrint1";
        String primaryMobile = "1234";
        String secondaryMobile = "567";
        String tertiaryMobile = "890";
        ProviderRequest providerRequest = new ProviderRequest(providerId, district, primaryMobile, null);
        providerRequest.setSecondaryMobile(secondaryMobile);
        providerRequest.setTertiaryMobile(tertiaryMobile);

        ImportProviderRequest importProviderRequest = new ImportProviderRequestBuilder().withProviderId(providerId)
                .withDistrict(district).withPrimaryNumber(primaryMobile).withSecondaryNumber(secondaryMobile)
                .withTertiaryNumber(tertiaryMobile).build();

        assertEquals(providerRequest,providerRequestMapper.map(importProviderRequest));
    }
}
