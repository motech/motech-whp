package org.motechproject.whp.controller;

import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.whp.patient.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private AllProviders allProviders;

    @Autowired
    public HomeController(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @RequestMapping("/")
    public String homePage(HttpServletRequest request) {
        AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER);
        return "redirect:/providers/" + allProviders.get(user.getExternalId()).getProviderId();
    }
}
