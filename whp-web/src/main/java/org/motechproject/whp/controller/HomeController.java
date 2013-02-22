package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class HomeController extends BaseWebController {

    private HomePageService homePageService;

    @Autowired
    public HomeController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void homePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MotechUser user = loggedInUser(request);

        List<String> userRoles = user.getRoles();
        String homePage = "/patients/listByProvider";

        if (homePageService.homePageFor(userRoles).isEmpty()) {
            String message = Flash.in("message", request);
            if (StringUtils.isNotEmpty(message)) {
                Flash.out("message", message, request);
            }
        } else {
            homePage = homePageService.homePageFor(userRoles).getHomePage();
        }
        response.sendRedirect(request.getContextPath() + response.encodeURL(homePage));
    }
}

