package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.whp.refdata.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorSeed  {
    @Autowired
    private AllMotechWebUsers allMotechWebUsers;

    @Seed(priority = 0)
    public void load() {
        Administrator administrator = new Administrator("Administrator", "admin", "password");
        allMotechWebUsers.add(administrator);
    }
}
