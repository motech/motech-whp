package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.mapper.ProviderReportingService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderReportSeed {

    private final ProviderService providerService;
    private final ProviderReportingService providerReportingService;

    @Autowired
    public ProviderReportSeed(ProviderService providerService, ProviderReportingService providerReportingService) {
        this.providerService = providerService;
        this.providerReportingService = providerReportingService;
    }

    @Seed(version = "5.0", priority = 0)
    public void migrateProviders() {
        List<Provider> providerList = providerService.getAll();
        for(Provider provider : providerList) {
            providerReportingService.reportProvider(provider);
        }
    }
}
