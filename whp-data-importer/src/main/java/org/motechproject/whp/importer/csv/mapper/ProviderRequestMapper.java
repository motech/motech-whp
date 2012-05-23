package org.motechproject.whp.importer.csv.mapper;

import org.motechproject.whp.importer.csv.request.ImportProviderRequest;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.springframework.stereotype.Component;

@Component
public class ProviderRequestMapper {
    public ProviderRequest map(ImportProviderRequest importProviderRequest){
        ProviderRequest providerRequest = new ProviderRequest(importProviderRequest.getProviderId(),importProviderRequest.getDistrict(), importProviderRequest.getPrimaryMobile(),null);
        providerRequest.setSecondaryMobile(importProviderRequest.getSecondaryMobile());
        providerRequest.setTertiaryMobile(importProviderRequest.getTertiaryMobile());
        return providerRequest;
    }
}
