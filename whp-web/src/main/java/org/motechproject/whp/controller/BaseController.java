package org.motechproject.whp.controller;

import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    protected MotechUser loggedInUser(HttpServletRequest request) {
        return (MotechUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
    }
}

