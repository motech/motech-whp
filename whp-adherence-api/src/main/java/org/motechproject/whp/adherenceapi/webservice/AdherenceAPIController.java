package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceFlashingWebService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


@Controller
@RequestMapping("/ivr/adherence")
public class AdherenceAPIController {

    private ProviderService providerService;
    private AdherenceFlashingWebService adherenceFlashingWebService;

    @Autowired
    public AdherenceAPIController(ProviderService providerService, AdherenceFlashingWebService adherenceFlashingWebService) {
        this.providerService = providerService;
        this.adherenceFlashingWebService = adherenceFlashingWebService;
    }

    @RequestMapping(value = "/flashing", method = RequestMethod.POST, produces = APPLICATION_XML_VALUE, consumes = APPLICATION_XML_VALUE)
    @ResponseBody
    public AdherenceFlashingResponse flashingRequest(@RequestBody AdherenceFlashingRequest request) {
        return adherenceFlashingWebService.processFlashingRequest(request, providerId(request.getMsisdn()));
    }

    private ProviderId providerId(String msisdn) {
        return new ProviderId(providerService.findByMobileNumber(msisdn));
    }

}
