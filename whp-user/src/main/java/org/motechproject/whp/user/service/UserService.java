package org.motechproject.whp.user.service;

import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    MotechAuthenticationService motechAuthenticationService;

    @Autowired
    public UserService(MotechAuthenticationService motechAuthenticationService){
        this.motechAuthenticationService = motechAuthenticationService;
    }
    public MotechUser changePassword(String userName, String currentPassword, String newPassword) {
        return motechAuthenticationService.changePassword(userName, currentPassword, newPassword);
    }

    public void activateUser(String userName) {
        motechAuthenticationService.activateUser(userName);
    }

    public boolean resetPassword(String userName, String password) {
        return motechAuthenticationService.resetPassword(userName,password);
    }

}
