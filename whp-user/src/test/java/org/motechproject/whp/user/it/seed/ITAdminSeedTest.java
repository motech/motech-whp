package org.motechproject.whp.user.it.seed;

import org.junit.Test;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.seed.ITAdminSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/applicationUserContext.xml")
public class ITAdminSeedTest extends SpringIntegrationTest {

    @Autowired
    ITAdminSeed itAdminSeed;
    
    @Test
    public void should(){
        try {
            itAdminSeed.load();
        } catch (WebSecurityException e) {
            e.printStackTrace();
        }

    }

}
