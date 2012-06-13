package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPRole;
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
        MotechWebUser user = loggedInUser(request);
        List<String> userRoles = user.getRoles();
        if (userRoles.contains(WHPRole.CMF_ADMIN.name())) {
            return "redirect:/patients/all";
        } else  if(userRoles.contains(WHPRole.IT_ADMIN.name()))  {
               return "itadmin/index";
        } else {
            String message = Flash.in("message", request);
            if (StringUtils.isNotEmpty(message)) {
                Flash.out("message", message, request);
            }
            return "redirect:/patients?provider=" + allProviders.get(user.getExternalId()).getProviderId();
        }
    }


}

