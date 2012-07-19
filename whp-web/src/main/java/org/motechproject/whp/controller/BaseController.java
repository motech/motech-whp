package org.motechproject.whp.controller;

import org.apache.log4j.Logger;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    protected MotechUser loggedInUser(HttpServletRequest request) {
        return (MotechUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        logger.error("Error occurred", ex);
        return "errors/error";
    }
}

