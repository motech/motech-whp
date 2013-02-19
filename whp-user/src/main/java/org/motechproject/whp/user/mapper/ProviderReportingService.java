package org.motechproject.whp.user.mapper;

import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.provider.ProviderDTO;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderReportingService {

    private ReportingPublisherService reportingPublisherService;

    @Autowired
    public ProviderReportingService(ReportingPublisherService reportingPublisherService) {
        this.reportingPublisherService = reportingPublisherService;
    }

    public void reportProvider(Provider provider) {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setProviderId(provider.getProviderId());
        providerDTO.setDistrict(provider.getDistrict());
        providerDTO.setPrimaryMobile(provider.getPrimaryMobile());
        providerDTO.setSecondaryMobile(provider.getSecondaryMobile());
        providerDTO.setTertiaryMobile(provider.getTertiaryMobile());

        reportingPublisherService.reportProvider(providerDTO);
    }
}
