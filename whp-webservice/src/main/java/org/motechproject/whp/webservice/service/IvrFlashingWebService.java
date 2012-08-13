package org.motechproject.whp.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.ivr.IvrCallService;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.contract.FlashingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

// Restful API to enable third party application to post an IVR callback request to provider for adherence capture
@Controller
@RequestMapping("/ivr/callback")
public class IvrFlashingWebService {
    private IvrCallService ivrCallService;
    private ProviderService providerService;

    @Autowired
    public IvrFlashingWebService(IvrCallService ivrCallService, ProviderService providerService) {
        this.ivrCallService = ivrCallService;
        this.providerService = providerService;
    }

    @RequestMapping(method = RequestMethod.POST, headers="Accept=application/xml")
    @ResponseStatus(value = HttpStatus.OK)
    public void callBack(@RequestBody FlashingRequest flashingRequest) {
        String trimmedMobileNumber = extractMobileNumber(flashingRequest.getMobileNumber());

        if(providerService.isRegisteredMobileNumber(trimmedMobileNumber))
            ivrCallService.initiateCall(trimmedMobileNumber);
    }

    private String extractMobileNumber(String phoneNumber) {
        return StringUtils.substring(phoneNumber, -10);
    }
}
