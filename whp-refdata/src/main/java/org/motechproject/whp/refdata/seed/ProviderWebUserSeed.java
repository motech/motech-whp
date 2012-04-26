package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProviderWebUserSeed {
    @Autowired
    private MotechAuthenticationService authenticationService;

    @Seed(priority = 1)
    public void load() {
        authenticationService.register("raj", "password", "WHPProviderUser", null, Arrays.asList(WHPRole.PROVIDER.name()));
    }
}
