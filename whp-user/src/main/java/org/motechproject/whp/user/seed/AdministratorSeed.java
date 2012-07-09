package org.motechproject.whp.user.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.user.domain.CmfAdmin;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.CmfAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdministratorSeed {

    @Autowired
    private MotechAuthenticationService authenticationService;
    @Autowired
    private CmfAdminService cmfAdminService;

    @Seed(priority = 0, version = "1.0")
    public void load() throws WebSecurityException {
        cmfAdminService.add(new CmfAdmin("admin", "password","test","Delhi","Cmf Admin1"),"password");
        authenticationService.register("itadmin1", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
        authenticationService.register("itadmin2", "password", null, Arrays.asList(WHPRole.IT_ADMIN.name()));
    }
}
