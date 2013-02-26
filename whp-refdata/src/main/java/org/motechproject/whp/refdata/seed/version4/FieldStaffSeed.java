package org.motechproject.whp.refdata.seed.version4;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.user.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FieldStaffSeed {

    @Autowired
    private MotechAuthenticationService motechAuthenticationService;

    @Seed(priority = 0, version = "4.0")
    public void load(){
        registerFieldStaff("Vaishali");
        registerFieldStaff("Nalanda");
        registerFieldStaff("Samastipur");
        registerFieldStaff("Gopalganj");
        registerFieldStaff("Patna");
        registerFieldStaff("East Champaran");
        registerFieldStaff("Muzaffarpur");
        registerFieldStaff("West Champaran");
        registerFieldStaff("Khagaria");
        registerFieldStaff("Saharsa");
        registerFieldStaff("Jehanabad");
        registerFieldStaff("Begusarai");
        registerFieldStaff("Bhagalpur");
        registerFieldStaff("Saran");
        registerFieldStaff("Siwan");
        registerFieldStaff("Darbhanga");
        registerFieldStaff("Madhubani");
        registerFieldStaff("Sitamarhi");
        registerFieldStaff("Purnea");
        registerFieldStaff("Gaya");
        registerFieldStaff("Araria");
        registerFieldStaff("Arwal");
        registerFieldStaff("Madhepura");
        registerFieldStaff("Aurangabad");
        registerFieldStaff("Monghyr");
        registerFieldStaff("Banka");
        registerFieldStaff("Nawada");
        registerFieldStaff("Bhojpur");
        registerFieldStaff("Buxar");
        registerFieldStaff("Rohtas");
        registerFieldStaff("Jamui");
        registerFieldStaff("Shiekhpura");
        registerFieldStaff("Sheohar");
        registerFieldStaff("Kaimur");
        registerFieldStaff("Katihar");
        registerFieldStaff("Supaul");
        registerFieldStaff("Kishanganj");
        registerFieldStaff("Lakhisarai");
    }

    private void registerFieldStaff(String district){
        try{
         motechAuthenticationService.register(district,"password",district, Arrays.asList(WHPRole.FIELD_STAFF.name()), true);
        } catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR, e.getMessage());
        }
    }
}
