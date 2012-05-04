package org.motechproject.whp.controller;

import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    protected AuthenticatedUser loggedInUser(HttpServletRequest request) {
        return (AuthenticatedUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
    }
}

