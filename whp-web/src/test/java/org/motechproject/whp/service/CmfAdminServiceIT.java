package org.motechproject.whp.service;

import org.junit.After;
import org.junit.Test;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.patient.domain.CmfAdmin;
import org.motechproject.whp.patient.repository.AllCmfAdmins;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class CmfAdminServiceIT {
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
        CmfAdmin cmfAdmin = new CmfAdmin("cmfadmin","a@b.com","","1","");
        cmfAdminService.add(cmfAdmin,"password");

        CmfAdmin dbAdmin = allCmfAdmins.findByUserId("cmfadmin");

        assertNotNull(dbAdmin);
        assertTrue(motechAuthenticationService.hasUser("cmfadmin"));

        assertFalse(cmfAdmin.getId().isEmpty());
        MotechWebUser motechWebUser = motechAuthenticationService.changePassword("cmfadmin", "password");
        assertEquals(cmfAdmin.getId(),motechWebUser);

        assertTrue(motechWebUser.getRoles().contains(WHPRole.CMF_ADMIN.name()));
    }
}
