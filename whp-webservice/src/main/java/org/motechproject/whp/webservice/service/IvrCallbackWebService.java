package org.motechproject.whp.webservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

// Restful API to enable third party application to post an IVR callback request to provider for adherence capture
@Controller
@RequestMapping("/ivr/callback")
public class IvrCallbackWebService {

    @RequestMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void doCallBack(@RequestParam String phoneNumber){

    }

}
