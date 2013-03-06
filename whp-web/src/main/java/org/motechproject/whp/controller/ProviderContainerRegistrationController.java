package org.motechproject.whp.controller;

import freemarker.template.TemplateException;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.ProviderContainerRegistrationValidator;
import org.motechproject.whp.user.domain.WHPRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/containerRegistration/by_provider")
public class ProviderContainerRegistrationController extends ContainerRegistrationController {

    @Autowired
    public ProviderContainerRegistrationController(ContainerService containerService,
                                                   ProviderContainerRegistrationValidator providerContainerRegistrationValidator) {
        super(containerService, providerContainerRegistrationValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model uiModel, HttpServletRequest request) {
        populateViewDetails(uiModel, request);
        return "containerRegistration/showForProvider";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model uiModel, ContainerRegistrationRequest registrationRequest,
                           HttpServletRequest servletRequest) throws IOException, TemplateException {
        populateProviderId(registrationRequest, servletRequest);
        populateChannelId(registrationRequest);
        populateSubmitterDetails(registrationRequest, servletRequest);
        if (validate(uiModel, registrationRequest)) {
            return show(uiModel, servletRequest);
        }

        containerService.registerContainer(registrationRequest);

        out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Container with id %s registered successfully.", registrationRequest.getContainerId()), servletRequest);
        return "redirect:/containerRegistration/by_provider";
    }

    private void populateProviderId(ContainerRegistrationRequest registrationRequest, HttpServletRequest request) {
         registrationRequest.setProviderId(loggedInUser(request).getUserName());
    }

    @Override
    String getSupportedUserRole() {
        return WHPRole.PROVIDER.name();
    }
}