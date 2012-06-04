package org.motechproject.whp.registration.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdministratorSeed {
    @Autowired
    private MotechAuthenticationService authenticationService;

    @Seed(priority = 0, version = "1.0")
    public void load() {
        authenticationService.register("admin", "password", null, Arrays.asList(WHPRole.CMF_ADMIN.name()));
        authenticationService.register("itadmin1", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
        authenticationService.register("itadmin2", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
    }
}
