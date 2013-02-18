package org.motechproject.whp.user.mapper;

import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderReportingRequestMapper {

    public ProviderDTO map(Provider provider) {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setProviderId(provider.getProviderId());
        providerDTO.setDistrict(provider.getDistrict());
        providerDTO.setPrimaryMobile(provider.getPrimaryMobile());
        providerDTO.setSecondaryMobile(provider.getSecondaryMobile());
        providerDTO.setTertiaryMobile(provider.getTertiaryMobile());

        return providerDTO;
    }
}
