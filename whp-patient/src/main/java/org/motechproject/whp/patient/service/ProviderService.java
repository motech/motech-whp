package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.CreateProviderRequest;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.mapper.ProviderMapper;
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

    public void add(CreateProviderRequest createProviderRequest) {
        Provider provider = new ProviderMapper().map(createProviderRequest);
        allProviders.addOrReplace(provider);
    }

}
