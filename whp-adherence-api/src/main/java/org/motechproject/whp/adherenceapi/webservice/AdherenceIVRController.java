package org.motechproject.whp.adherenceapi.webservice;

import org.apache.log4j.Logger;
import org.motechproject.whp.adherenceapi.adherence.*;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.*;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceCallStatusValidationResponse;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.common.error.BindingResultXML;
import org.motechproject.whp.user.domain.Provider;
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
    private AdherenceNotCapturedOverIVR adherenceNotCapturedOverIVR;
    private AdherenceCallStatusOverIVR adherenceCallStatusOverIVR;
    private Logger logger = Logger.getLogger(AdherenceIVRController.class);

    @Autowired
    public AdherenceIVRController(ProviderService providerService,
                                  AdherenceSummaryOverIVR adherenceSummaryOverIVR,
                                  AdherenceValidationOverIVR adherenceValidationOverIVR, AdherenceConfirmationOverIVR adherenceConfirmationOverIVR, AdherenceNotCapturedOverIVR adherenceNotCapturedOverIVR, AdherenceCallStatusOverIVR adherenceCallStatusOverIVR) {

        this.providerService = providerService;
        this.adherenceSummaryOverIVR = adherenceSummaryOverIVR;
        this.adherenceValidationOverIVR = adherenceValidationOverIVR;
        this.adherenceConfirmationOverIVR = adherenceConfirmationOverIVR;
        this.adherenceNotCapturedOverIVR = adherenceNotCapturedOverIVR;
        this.adherenceCallStatusOverIVR = adherenceCallStatusOverIVR;
    }

    @RequestMapping(value = "/summary")
    @ResponseBody
    public AdherenceFlashingResponse adherenceSummary(@RequestBody @Valid AdherenceFlashingRequest request) {
        return adherenceSummaryOverIVR.value(request, providerIdFromMsisdn(request.getMsisdn()));
    }

    @RequestMapping(value = "/validate")
    @ResponseBody
    public AdherenceValidationResponse adherenceValidation(@RequestBody @Valid AdherenceValidationRequest request) {
        return adherenceValidationOverIVR.handleValidationRequest(request, providerId(request.getProviderId()));
    }

    @RequestMapping(value = "/confirm")
    @ResponseBody
    public AdherenceValidationResponse adherenceConfirmation(@RequestBody @Valid AdherenceConfirmationRequest request) {
        AdherenceValidationResponse response = adherenceConfirmationOverIVR.confirmAdherence(request, providerId(request.getProviderId()));
        if (response.failed())
            return response;
        return null;
    }

    @RequestMapping(value = "/notcaptured")
    @ResponseBody
    public AdherenceValidationResponse adherenceNotCaptured(@RequestBody @Valid AdherenceNotCapturedRequest request) {
        AdherenceValidationResponse response = adherenceNotCapturedOverIVR.recordNotCaptured(request, providerId(request.getProviderId()));
        if (response.failed())
            return response;
        return null;
    }

    @RequestMapping(value = "/callstatus")
    @ResponseBody
    public AdherenceCallStatusValidationResponse adherenceCallStatus(@RequestBody @Valid AdherenceCallStatusRequest request) {
        AdherenceCallStatusValidationResponse response = adherenceCallStatusOverIVR.recordCallStatus(request);
        if (response.failed())
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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException(Exception ex) {
        logger.error("Error occurred", ex);
    }

    private ProviderId providerId(String providerId) {
        return new ProviderId(providerId);
    }

    private ProviderId providerIdFromMsisdn(String msisdn) {
        Provider provider = providerService.findByMobileNumber(msisdn);
        if(provider != null)
        return new ProviderId(provider.getProviderId());

        return new ProviderId();
    }
}
