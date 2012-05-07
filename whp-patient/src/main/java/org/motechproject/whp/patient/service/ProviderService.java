package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    AllProviders allProviders;

    @Autowired
    public ProviderService(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    public String add(String providerId, String primaryMobile, String secondaryMobile, String tertiaryMobile, String district, DateTime lastModifiedDate) {
        Provider provider = new Provider(providerId, primaryMobile, district, lastModifiedDate);
        provider.setSecondaryMobile(secondaryMobile);
        provider.setTertiaryMobile(tertiaryMobile);
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

}
