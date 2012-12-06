package org.motechproject.whp.adherenceapi.webservice;

import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.adherenceapi.response.AdherenceCaptureFlashingResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceWebService;
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

    private AdherenceWebService adherenceWebService;

    @Autowired
    public AdherenceAPIController(AdherenceWebService adherenceWebService) {
        this.adherenceWebService = adherenceWebService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_XML_VALUE, consumes = APPLICATION_XML_VALUE)
    @ResponseBody
    public AdherenceCaptureFlashingResponse adherenceSubmission(@RequestBody AdherenceCaptureFlashingRequest request) {
        return adherenceWebService.processFlashingRequest(request, today());
    }

}
