package org.motechproject.whp.refdata.seed.version2;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.user.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ITAdminSeed {

    @Autowired
    private MotechAuthenticationService authenticationService;

    @Seed(priority = 0, version = "2.0")
    public void load() throws WebSecurityException {
        authenticationService.register("itadmin1", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
        authenticationService.register("itadmin2", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
    }
}
