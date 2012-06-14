package org.motechproject.whp.registration.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderSeed {

    @Autowired
    private RegistrationService registrationService;

    // Need not have this, till we create logins for acutal providers
    @Seed(priority = 1, version = "1.0")
    public void load() {
        registrationService.registerProvider(new ProviderRequest("raj", "district", "1234567890", DateUtil.now()));
        registrationService.activateUser("raj");
    }

}
