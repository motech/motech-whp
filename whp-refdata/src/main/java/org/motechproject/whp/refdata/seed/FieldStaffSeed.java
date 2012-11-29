package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FieldStaffSeed {

    @Autowired
    private MotechAuthenticationService motechAuthenticationService;

    @Seed(priority = 0, version = "4.0")
    public void load(){
        registerFieldStaff("Begusarai");
        registerFieldStaff("Jehanabad");
    }

    private void registerFieldStaff(String district){
        try{
         motechAuthenticationService.register(district,"password",district, Arrays.asList(WHPRole.FIELD_STAFF.name()), true);
        } catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR, e.getMessage());
        }
    }
}
