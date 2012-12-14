package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.adherence.AdherenceConfirmationOverIVR;
import org.motechproject.whp.adherenceapi.adherence.AdherenceSummaryOverIVR;
import org.motechproject.whp.adherenceapi.adherence.AdherenceValidationOverIVR;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceConfirmationRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.common.error.BindingResultXML;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


@Controller
@RequestMapping(value = "/ivr/adherence",
        method = RequestMethod.POST,
        produces = APPLICATION_XML_VALUE,
        consumes = APPLICATION_XML_VALUE)
public class AdherenceIVRController {

    private ProviderService providerService;
    private AdherenceSummaryOverIVR adherenceSummaryOverIVR;
    private AdherenceValidationOverIVR adherenceValidationOverIVR;
    private AdherenceConfirmationOverIVR adherenceConfirmationOverIVR;

    @Autowired
    public AdherenceIVRController(ProviderService providerService,
                                  AdherenceSummaryOverIVR adherenceSummaryOverIVR,
                                  AdherenceValidationOverIVR adherenceValidationOverIVR, AdherenceConfirmationOverIVR adherenceConfirmationOverIVR) {

        this.providerService = providerService;
        this.adherenceSummaryOverIVR = adherenceSummaryOverIVR;
        this.adherenceValidationOverIVR = adherenceValidationOverIVR;
        this.adherenceConfirmationOverIVR = adherenceConfirmationOverIVR;
    }

    @RequestMapping(value = "/summary")
    @ResponseBody
    public AdherenceFlashingResponse adherenceSummary(@RequestBody @Valid AdherenceFlashingRequest request) {
        return adherenceSummaryOverIVR.value(request, providerId(request.getMsisdn()));
    }

    @RequestMapping(value = "/validate")
    @ResponseBody
    public AdherenceValidationResponse adherenceValidation(@RequestBody @Valid AdherenceValidationRequest request) {
        return adherenceValidationOverIVR.handleValidationRequest(request, providerId(request.getMsisdn()));
    }

    @RequestMapping(value = "/confirm")
    @ResponseBody
    public AdherenceValidationResponse adherenceConfirmation(@RequestBody @Valid AdherenceConfirmationRequest request) {
        AdherenceValidationResponse response = adherenceConfirmationOverIVR.confirmAdherence(request, providerId(request.getMsisdn()));
        if(response.failed())
            return response;
        return null;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BindingResultXML handleError(MethodArgumentNotValidException e, HttpServletResponse response) {
        response.setContentType(APPLICATION_XML_VALUE);
        return new BindingResultXML(e.getBindingResult());
    }

    private ProviderId providerId(String msisdn) {
        return new ProviderId(providerService.findByMobileNumber(msisdn));
    }
}
