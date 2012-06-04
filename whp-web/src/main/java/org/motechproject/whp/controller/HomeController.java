package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.Roles;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    private AllProviders allProviders;

    @Autowired
    public HomeController(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @RequestMapping("/")
    public String homePage(HttpServletRequest request) {
        AuthenticatedUser user = loggedInUser(request);
        Roles userRoles = user.getRoles();
        if (userRoles.hasRole(WHPRole.CMF_ADMIN.name())) {
            return "admin";
        } else  if(userRoles.hasRole(WHPRole.IT_ADMIN.name()))  {
               return "itadmin";
        } else {
            String message = Flash.in("message", request);
            if (StringUtils.isNotEmpty(message)) {
                Flash.out("message", message, request);
            }
            return "redirect:/patients?provider=" + allProviders.get(user.getExternalId()).getProviderId();
        }
    }
}

