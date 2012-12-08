package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.adherence.AdherenceSummaryOverIVR;
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
public class AdherenceIVRController {

    private ProviderService providerService;
    private AdherenceSummaryOverIVR adherenceSummaryOverIVR;

    @Autowired
    public AdherenceIVRController(ProviderService providerService, AdherenceSummaryOverIVR adherenceSummaryOverIVR) {
        this.providerService = providerService;
        this.adherenceSummaryOverIVR = adherenceSummaryOverIVR;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST, produces = APPLICATION_XML_VALUE, consumes = APPLICATION_XML_VALUE)
    @ResponseBody
    public AdherenceFlashingResponse adherenceSummary(@RequestBody AdherenceFlashingRequest request) {
        return adherenceSummaryOverIVR.value(request, providerId(request.getMsisdn()));
    }

    private ProviderId providerId(String msisdn) {
        return new ProviderId(providerService.findByMobileNumber(msisdn));
    }

}
