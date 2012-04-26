package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.CreateProviderRequest;
import org.motechproject.whp.patient.domain.Provider;

public class ProviderMapper {

    public Provider map(CreateProviderRequest providerRequest) {
        Provider provider = new Provider(
                providerRequest.getProviderId(),
                providerRequest.getPrimaryMobile(),
                providerRequest.getDistrict(),
                providerRequest.getLastModifiedDate());
        provider.setSecondaryMobile(providerRequest.getSecondaryMobile());
        provider.setTertiaryMobile(providerRequest.getTertiaryMobile());

        return provider;
    }

}
