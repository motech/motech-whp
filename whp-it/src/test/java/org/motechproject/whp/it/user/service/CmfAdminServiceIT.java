package org.motechproject.whp.it.user.service;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.domain.CmfAdmin;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllCmfAdmins;
import org.motechproject.whp.user.service.CmfAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationUserContext.xml")
public class CmfAdminServiceIT extends SpringIntegrationTest {

    @Autowired
    CmfAdminService cmfAdminService;

    @Autowired
    MotechAuthenticationService motechAuthenticationService;

    @Autowired
    AllCmfAdmins allCmfAdmins;

    @After
    public void setup() {
        allCmfAdmins.removeAll();
        motechAuthenticationService.remove("cmfadmin");
    }

    @Test
    public void shouldAddUser() throws WebSecurityException {
        CmfAdmin cmfAdmin = new CmfAdmin("cmfadmin", "a@b.com", "", "1", "");
        cmfAdminService.add(cmfAdmin, "password");

        CmfAdmin dbAdmin = allCmfAdmins.findByUserId("cmfadmin");

        assertNotNull(dbAdmin);
        assertTrue(motechAuthenticationService.hasUser("cmfadmin"));

        assertFalse(cmfAdmin.getId().isEmpty());
        MotechUser user = motechAuthenticationService.retrieveUserByCredentials("cmfadmin", "password");
        assertEquals(cmfAdmin.getId(), user.getExternalId());

        assertTrue(user.getRoles().contains(WHPRole.CMF_ADMIN.name()));
    }

}
