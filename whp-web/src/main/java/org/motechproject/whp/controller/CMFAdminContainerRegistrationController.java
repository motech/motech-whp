package org.motechproject.whp.controller;

import freemarker.template.TemplateException;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.CmfAdminContainerRegistrationRequest;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerRegistrationRequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/containerRegistration/by_cmfAdmin")
public class CmfAdminContainerRegistrationController extends ContainerRegistrationController {

    @Autowired
    public CmfAdminContainerRegistrationController(ContainerService containerService, ContainerRegistrationRequestValidator registrationRequestValidator, SputumTrackingProperties sputumTrackingProperties) {
        super(containerService, registrationRequestValidator, sputumTrackingProperties);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model uiModel, HttpServletRequest request) {
        populateViewDetails(uiModel, request);
        return "containerRegistration/showForCmfAdmin";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model uiModel, CmfAdminContainerRegistrationRequest registrationRequest, HttpServletRequest servletRequest) throws IOException, TemplateException {
        if (hasErrors(uiModel, registrationRequest, servletRequest))
            return show(uiModel, servletRequest);

        containerService.registerContainer(registrationRequest);

        out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Container with id %s registered successfully.", registrationRequest.getContainerId()), servletRequest);
        return "redirect:/containerRegistration/by_cmfAdmin";
    }
}