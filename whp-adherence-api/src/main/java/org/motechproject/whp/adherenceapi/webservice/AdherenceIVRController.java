package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.adherence.AdherenceRecordingOverIVR;
import org.motechproject.whp.adherenceapi.adherence.AdherenceSummaryOverIVR;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


@Controller
@RequestMapping("/ivr/adherence")
public class AdherenceIVRController {

    private ProviderService providerService;
    private AdherenceSummaryOverIVR adherenceSummaryOverIVR;
    private AdherenceRecordingOverIVR adherenceRecordingOverIVR;

    @Autowired
    public AdherenceIVRController(ProviderService providerService,
                                  AdherenceSummaryOverIVR adherenceSummaryOverIVR,
                                  AdherenceRecordingOverIVR adherenceRecordingOverIVR) {

        this.providerService = providerService;
        this.adherenceSummaryOverIVR = adherenceSummaryOverIVR;
        this.adherenceRecordingOverIVR = adherenceRecordingOverIVR;
    }

    @RequestMapping(
            value = "/summary",
            method = RequestMethod.POST,
            produces = APPLICATION_XML_VALUE,
            consumes = APPLICATION_XML_VALUE
    )
    @ResponseBody
    public AdherenceFlashingResponse adherenceSummary(@RequestBody @Valid AdherenceFlashingRequest request) {
        return adherenceSummaryOverIVR.value(request, providerId(request.getMsisdn()));
    }

    @RequestMapping(
            value = "/validate",
            method = RequestMethod.POST,
            produces = APPLICATION_XML_VALUE,
            consumes = APPLICATION_XML_VALUE
    )
    @ResponseBody
    public AdherenceValidationResponse adherenceValidation(@RequestBody @Valid AdherenceValidationRequest request) {
        return adherenceRecordingOverIVR.validateInput(request, providerId(request.getMsisdn()));
    }

    private ProviderId providerId(String msisdn) {
        return new ProviderId(providerService.findByMobileNumber(msisdn));
    }
}
