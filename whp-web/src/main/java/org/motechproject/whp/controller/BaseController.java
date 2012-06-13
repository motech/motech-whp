package org.motechproject.whp.controller;

import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    protected MotechWebUser loggedInUser(HttpServletRequest request) {
        return (MotechWebUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
    }
}

