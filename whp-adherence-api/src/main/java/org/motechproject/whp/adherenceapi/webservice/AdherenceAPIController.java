package org.motechproject.whp.adherenceapi.webservice;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.reporting.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.motechproject.util.DateUtil.today;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


@Controller
@RequestMapping("/adherenceSubmission/")
public class AdherenceAPIController {

    private AdherenceService adherenceService;
    private ProviderService providerService;
    private AdherenceWindow adherenceWindow;
    private ReportingPublisherService reportingPublisherService;

    @Autowired
    public AdherenceAPIController(AdherenceService adherenceService, ProviderService providerService, AdherenceWindow adherenceWindow, ReportingPublisherService reportingPublisherService) {
        this.adherenceService = adherenceService;
        this.providerService = providerService;
        this.adherenceWindow = adherenceWindow;
        this.reportingPublisherService = reportingPublisherService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_XML_VALUE, consumes = APPLICATION_XML_VALUE)
    @ResponseBody
    public AdherenceCaptureFlashingResponse adherenceSubmission(@RequestBody AdherenceCaptureFlashingRequest request) {
        Provider provider = providerService.findByMobileNumber(request.getMsisdn());
        LocalDate today = today();
        if (provider == null) {
            return AdherenceCaptureFlashingResponse.failureResponse("INVALID_MOBILE_NUMBER");
        } else if (!adherenceWindow.isValidAdherenceDay(today)) {
            return AdherenceCaptureFlashingResponse.failureResponse("NON_ADHERENCE_DAY");
        } else {
            AdherenceCaptureFlashingResponse response = adherenceService.adherenceSummary(provider.getProviderId(), today);
            reportingPublisherService.reportFlashingRequest(new AdherenceFlashingRequest(request, provider.getProviderId()).flashingLogRequest());
            return response;
        }
    }

}
