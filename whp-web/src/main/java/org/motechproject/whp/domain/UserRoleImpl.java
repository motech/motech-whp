package org.motechproject.whp.domain;

import org.motechproject.user.management.domain.UserRoles;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.whp.user.domain.WHPRole.*;

@Component
public class UserRoleImpl implements UserRoles{
    @Override
    public List<String> all() {
        return asList(PROVIDER.name(), CMF_ADMIN.name(), FIELD_STAFF.name(), IT_ADMIN.name());
    }
}
