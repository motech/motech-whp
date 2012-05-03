package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.patient.builder.ProviderBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProviderSeed {

    @Autowired
    private MotechAuthenticationService authenticationService;
    @Autowired
    private AllProviders allProviders;

    // Need not have this, till we create logins for acutal providers
    @Seed(priority = 1)
    public void load() {
        Provider provider = createProvider();
        authenticationService.register("raj", "password", provider.getId(), Arrays.asList(WHPRole.PROVIDER.name()));
    }

    private Provider createProvider() {
        Provider provider = ProviderBuilder.startRecording().withDefaults().withProviderId("raj").build();
        allProviders.add(provider);
        return provider;
    }
}
