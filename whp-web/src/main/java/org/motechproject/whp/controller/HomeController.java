package org.motechproject.whp.controller;

import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String homePage(HttpServletRequest request) {
        AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
        return "redirect:/patient";
    }
}
