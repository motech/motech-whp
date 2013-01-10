package org.motechproject.whp.controller;

import org.apache.log4j.Logger;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class BaseWebController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    protected MotechUser loggedInUser(HttpServletRequest request) {
        return (MotechUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        logger.error("Error occurred", ex);
        return "errors/error";
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public List<FieldError> handleError(MethodArgumentNotValidException e) {
        logger.error("Bad Request \n" + e.getBindingResult().toString());
        return e.getBindingResult().getFieldErrors();
    }

    protected void setCookieValue(HttpServletResponse response, String name, String value) {
        response.addCookie(new Cookie(name, value));
    }
}

