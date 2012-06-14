package org.motechproject.whp.service;

import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.patient.domain.CmfAdmin;
import org.motechproject.whp.patient.repository.AllCmfAdmins;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CmfAdminService {
    @Autowired
    private AllCmfAdmins allCmfAdmins;

    @Autowired
    private MotechAuthenticationService motechAuthenticationService;

    @Autowired
    public CmfAdminService (AllCmfAdmins allCmfAdmins, MotechAuthenticationService motechAuthenticationService){

        this.allCmfAdmins = allCmfAdmins;
        this.motechAuthenticationService = motechAuthenticationService;
    }
    public void add(CmfAdmin admin, String password) throws WebSecurityException {
        allCmfAdmins.addOrReplace(admin);
        motechAuthenticationService.register(admin.getUserId(), password, admin.getId(), Arrays.asList(WHPRole.CMF_ADMIN.name()));
    }
}
