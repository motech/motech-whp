package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class HomeController extends BaseController {

    private AllProviders allProviders;

    @Autowired
    public HomeController(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public String homePage(HttpServletRequest request) {
        MotechUser user = loggedInUser(request);
        List<String> userRoles = user.getRoles();
        if (userRoles.contains(WHPRole.CMF_ADMIN.name())) {
            return "redirect:/patients/list";
        } else  if(userRoles.contains(WHPRole.IT_ADMIN.name()))  {
            return "redirect:/providers/list";
        } else {
            String message = Flash.in("message", request);
            if (StringUtils.isNotEmpty(message)) {
                Flash.out("message", message, request);
            }
            return "redirect:/patients?provider=" + allProviders.get(user.getExternalId()).getProviderId();
        }
    }


}

