package org.motechproject.whp.controller;

import freemarker.template.TemplateException;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.CmfAdminContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerRegistrationMode;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.CmfAdminContainerRegistrationValidator;
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
@RequestMapping(value = "/containerRegistration/by_cmfAdmin")
public class CmfAdminContainerRegistrationController extends ContainerRegistrationController {

    @Autowired
    public CmfAdminContainerRegistrationController(ContainerService containerService,
                                                   CmfAdminContainerRegistrationValidator cmfAdminContainerRegistrationValidator) {
        super(containerService, cmfAdminContainerRegistrationValidator);
    }

    @RequestMapping(value = "/new-container", method = RequestMethod.GET)
    public String newContainer(Model uiModel, HttpServletRequest request) {
        populateViewDetails(uiModel, request);
        return "containerRegistration/cmfAdminNewContainerRegistration";
    }

    @RequestMapping(value = "/on-behalf-of-provider", method = RequestMethod.GET)
    public String onBehalfOfProvider(Model uiModel, HttpServletRequest request) {
        populateViewDetails(uiModel, request);
        return "containerRegistration/cmfAdminContainerRegistrationBehalfOfProvider";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model uiModel, CmfAdminContainerRegistrationRequest registrationRequest, HttpServletRequest servletRequest) throws IOException, TemplateException {
        ContainerRegistrationMode mode = registrationRequest.getContainerRegistrationMode();
        if (validate(uiModel, registrationRequest)) {
            if (mode == ContainerRegistrationMode.NEW_CONTAINER)
                return newContainer(uiModel, servletRequest);
            return onBehalfOfProvider(uiModel, servletRequest);
        }
        populateSubmitterDetails(registrationRequest, servletRequest);
        populateChannelId(registrationRequest);
        containerService.registerContainer(registrationRequest);

        out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Container with id %s registered successfully.", registrationRequest.getContainerId()), servletRequest);
        String redirectedUrl = (mode == ContainerRegistrationMode.NEW_CONTAINER) ? "/new-container" : "/on-behalf-of-provider";
        return "redirect:/containerRegistration/by_cmfAdmin" +  redirectedUrl;
    }

    @Override
    String getSupportedUserRole() {
        return WHPRole.CMF_ADMIN.name();
    }
}