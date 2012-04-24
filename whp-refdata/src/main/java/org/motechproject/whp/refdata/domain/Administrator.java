package org.motechproject.whp.refdata.domain;

import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;

import java.util.Arrays;

public class Administrator extends MotechWebUser {

    public Administrator(String name, String userName, String password) {
        super(name, userName, password, Arrays.asList(new Role(WHPRole.ADMIN.name())));
    }
}
