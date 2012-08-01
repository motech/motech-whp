package org.motechproject.whp.user.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderSeed {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private UserService userService;

    // Need not have this, till we create logins for acutal providers
    @Seed(priority = 0, version = "1.0")
    public void load() {
        providerService.registerProvider(new ProviderRequest("raj", "Begusarai", "1234567890", DateUtil.now()));
        userService.activateUser("raj");
    }

}
