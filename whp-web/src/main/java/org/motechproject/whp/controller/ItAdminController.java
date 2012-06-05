package org.motechproject.whp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/itadmin")
public class ItAdminController {

    @RequestMapping(method = RequestMethod.GET)
    public String homePage(HttpServletRequest request) {
        return "itadmin/itadmin";
    }

}
