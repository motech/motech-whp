package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.whp.refdata.domain.ProviderWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderWebUserSeed {
    @Autowired
    private AllMotechWebUsers allMotechWebUsers;

    @Seed(priority = 0)
    public void load() {
        ProviderWebUser raj = new ProviderWebUser("Raj", "cha011", "password", "cha011");
        allMotechWebUsers.add(raj);
    }
}
