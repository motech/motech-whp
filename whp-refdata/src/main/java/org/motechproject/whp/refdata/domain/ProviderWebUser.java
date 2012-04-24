package org.motechproject.whp.refdata.domain;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;

import java.util.Arrays;

public class ProviderWebUser extends MotechWebUser {
    
    @Getter
    @Setter
    private String providerId;

    public ProviderWebUser(String name, String userName, String password, String providerId) {
        super(name, userName, password, Arrays.asList(new Role(WHPRole.PROVIDER.name())));
        this.providerId = providerId;
    }
}
