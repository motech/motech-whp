package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Instance;
import org.motechproject.whp.container.domain.RegistrationRequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/containerRegistration")
public class ContainerRegistrationController extends BaseWebController {

    public static final String INSTANCES = "instances";
    private ContainerService containerService;
    private RegistrationRequestValidator registrationRequestValidator;

    @Autowired
    public ContainerRegistrationController(ContainerService containerService, RegistrationRequestValidator registrationRequestValidator) {
        this.containerService = containerService;
        this.registrationRequestValidator = registrationRequestValidator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model uiModel, HttpServletRequest request) {
        populateWithInstances(uiModel, request);

        return "containerRegistration/show";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model uiModel, RegistrationRequest registrationRequest, HttpServletRequest request) {
        List<String> errors = registrationRequestValidator.validate(registrationRequest);
        if (!errors.isEmpty()) {
            uiModel.addAttribute("errors", StringUtils.join(errors, ","));
            show(uiModel, request);
            return "containerRegistration/show";
        }

        registrationRequest.setProviderId(loggedInUser(request).getUserName());
        containerService.registerContainer(registrationRequest);
        out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Container with id %s registered successfully.", registrationRequest.getContainerId()), request);
        return "redirect:/containerRegistration";
    }

    private void populateWithInstances(Model uiModel, HttpServletRequest request) {
        ArrayList<String> instances = new ArrayList<>();
        for (Instance instance : Instance.values())
            instances.add(instance.getDisplayText());
        uiModel.addAttribute(INSTANCES, instances);

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotBlank(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
    }
}

