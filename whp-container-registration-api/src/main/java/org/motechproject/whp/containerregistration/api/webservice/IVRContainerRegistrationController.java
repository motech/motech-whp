package org.motechproject.whp.containerregistration.api.webservice;


import freemarker.template.TemplateException;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.response.ValidationErrorResponse;
import org.motechproject.whp.containerregistration.response.VerificationResponse;
import org.motechproject.whp.containerregistration.service.IVRContainerRegistrationService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/ivr/containerRegistration")
public class IVRContainerRegistrationController {

    public static final String CONTENT_TYPE = "application/xml";
    private ContainerService containerService;
    private ProviderService providerService;
    private IVRContainerRegistrationService ivrContainerRegistrationService;

    @Autowired
    public IVRContainerRegistrationController(ContainerService containerService, ProviderService providerService, IVRContainerRegistrationService ivrContainerRegistrationService) {
        this.containerService = containerService;
        this.providerService = providerService;
        this.ivrContainerRegistrationService = ivrContainerRegistrationService;
    }

    @RequestMapping(value = "provider/verify", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse verifyProvider(@RequestBody ProviderVerificationRequest request, HttpServletResponse response) {
        VerificationResult verificationResult = ivrContainerRegistrationService.verify(request);
        return getVerificationResponse(verificationResult, response);
    }

    @RequestMapping(value = "container/verify", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse verifyContainer(@RequestBody ContainerVerificationRequest request, HttpServletResponse response) {
        VerificationResult verificationResult = ivrContainerRegistrationService.verify(request);
        return getVerificationResponse(verificationResult, response);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse registerThroughIVR(@RequestBody IvrContainerRegistrationRequest ivrContainerRegistrationRequest, HttpServletResponse response) throws IOException, TemplateException {
        VerificationResult verificationResult = ivrContainerRegistrationService.verify(ivrContainerRegistrationRequest);
        return getVerificationResponse(verificationResult, response);
    }

    private VerificationResponse getVerificationResponse(VerificationResult verificationResult, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);
        if (verificationResult.isSuccess()) {
            return new VerificationResponse(verificationResult);
        } else {
            return new ValidationErrorResponse(verificationResult);
        }
    }
}
