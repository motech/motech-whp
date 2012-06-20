package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.user.domain.CmfAdmin;

import static junit.framework.Assert.assertEquals;

public class CmfAdminTest {

    @Test
    public void shouldsetUserNameToLowerCase() {
        CmfAdmin cmfAdmin = new CmfAdmin("USER", "a@b.com", "dept", null, "staffName");
        assertEquals("user", cmfAdmin.getUserId());
    }
}
