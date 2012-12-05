package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.motechproject.util.DateUtil.today;


@Controller
@RequestMapping("/adherenceSubmission/")
public class AdherenceAPIController {

    public static final String XML_CONTENT_TYPE = "application/xml";

    private AdherenceService adherenceService;
    private ProviderService providerService;

    @Autowired
    public AdherenceAPIController(AdherenceService adherenceService, ProviderService providerService) {
        this.adherenceService = adherenceService;
        this.providerService = providerService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = XML_CONTENT_TYPE, consumes = XML_CONTENT_TYPE)
    @ResponseBody
    public AdherenceCaptureFlashingResponse adherenceSubmission(@RequestBody AdherenceCaptureFlashingRequest request) {
        Provider provider = providerService.findByMobileNumber(request.getMsisdn());
        if (provider == null) {
            return AdherenceCaptureFlashingResponse.invalidMSISDN();
        }
        return adherenceService.adherenceSummary(provider.getProviderId(), today());
    }
}
