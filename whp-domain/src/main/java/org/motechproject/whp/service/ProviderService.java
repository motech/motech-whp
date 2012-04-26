package org.motechproject.whp.service;

import org.motechproject.whp.domain.Provider;
import org.motechproject.whp.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    AllProviders allProviders;

    @Autowired
    public ProviderService(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    public void add(Provider provider) {
        allProviders.addOrReplace(provider);
    }

}
