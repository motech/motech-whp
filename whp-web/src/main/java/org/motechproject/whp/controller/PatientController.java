package org.motechproject.whp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/patient")
public class PatientController {

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "patient";
    }
}
