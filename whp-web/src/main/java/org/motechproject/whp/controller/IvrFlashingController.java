package org.motechproject.whp.controller;

import org.motechproject.whp.ivr.IvrCallService;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.request.IvrFlashingWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/ivr/callback")
public class IvrFlashingController {
    private IvrCallService ivrCallService;

    @Autowired
    public IvrFlashingController(IvrCallService ivrCallService) {
        this.ivrCallService = ivrCallService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void callBack(@RequestBody IvrFlashingWebRequest ivrFlashingWebRequest) {
        FlashingRequest flashingRequest = ivrFlashingWebRequest.createFlashingRequest();
        ivrCallService.handleFlashingRequest(flashingRequest);
    }
}
