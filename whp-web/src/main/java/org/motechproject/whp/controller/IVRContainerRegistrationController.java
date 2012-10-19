package org.motechproject.whp.controller;


import freemarker.template.TemplateException;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.mapper.ContainerRegistrationRequestMapper;
import org.motechproject.whp.reponse.ValidationErrorResponse;
import org.motechproject.whp.reponse.VerificationResponse;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.motechproject.whp.wgninbound.verification.ContainerRegistrationVerification;
import org.motechproject.whp.wgninbound.verification.ContainerVerification;
import org.motechproject.whp.wgninbound.verification.ProviderVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/sputumCall")
public class IVRContainerRegistrationController {

    public static final String CONTENT_TYPE = "application/xml";
    private ProviderVerification providerVerification;
    private ContainerVerification containerVerification;
    private ContainerRegistrationVerification containerRegistrationVerification;
    private ContainerService containerService;
    private ProviderService providerService;

    @Autowired
    public IVRContainerRegistrationController(ProviderVerification providerVerification, ContainerVerification containerVerification,
                                              ContainerRegistrationVerification containerRegistrationVerification, ContainerService containerService, ProviderService providerService) {
        this.providerVerification = providerVerification;
        this.containerVerification = containerVerification;
        this.containerRegistrationVerification = containerRegistrationVerification;
        this.containerService = containerService;
        this.providerService = providerService;
    }

    @RequestMapping(value = "provider/verify", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse verifyProvider(@RequestBody ProviderVerificationRequest request, HttpServletResponse response) {
        VerificationResult verificationResult = providerVerification.verifyRequest(request);
        return getVerificationResponse(verificationResult, response);
    }

    @RequestMapping(value = "container/verify", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse verifyContainer(@RequestBody ContainerVerificationRequest request, HttpServletResponse response) {
        VerificationResult verificationResult = containerVerification.verifyRequest(request);
        return getVerificationResponse(verificationResult, response);
    }

    @RequestMapping(value = "/registerContainer", method = RequestMethod.POST)
    @ResponseBody
    public VerificationResponse registerThroughIVR(@RequestBody IvrContainerRegistrationRequest ivrContainerRegistrationRequest, HttpServletResponse response) throws IOException, TemplateException {

        VerificationResult verificationResult = containerRegistrationVerification.verifyRequest(ivrContainerRegistrationRequest);

        if (verificationResult.isSuccess()) {
            ContainerRegistrationRequestMapper containerRegistrationRequestMapper = new ContainerRegistrationRequestMapper(providerService);
            ContainerRegistrationRequest containerRegistrationRequest = containerRegistrationRequestMapper.buildContainerRegistrationRequest(ivrContainerRegistrationRequest);
            containerService.registerContainer(containerRegistrationRequest);
        }

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
