package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.service.HomePageService;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class HomeController extends BaseWebController {

    private AllProviders allProviders;
    private HomePageService homePageService;

    @Autowired
    public HomeController(AllProviders allProviders, HomePageService homePageService) {
        this.allProviders = allProviders;
        this.homePageService = homePageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(HttpServletRequest request, HttpServletResponse response) {
        MotechUser user = loggedInUser(request);
        List<String> userRoles = user.getRoles();
        if (homePageService.homePageFor(userRoles).isEmpty()) {
            String message = Flash.in("message", request);
            if (StringUtils.isNotEmpty(message)) {
                Flash.out("message", message, request);
            }
            return "redirect:/patients/listByProvider";
        } else {
            return "redirect:" + homePageService.homePageFor(userRoles).getHomePage();
        }
    }
}

