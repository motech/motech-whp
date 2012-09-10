package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Instance;
import org.motechproject.whp.container.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/containerRegistration")
public class ContainerRegistrationController extends BaseWebController {

    public static final String INSTANCES = "instances";
    private ContainerService containerService;

    @Autowired
    public ContainerRegistrationController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model uiModel, HttpServletRequest request) {
        populateWithInstances(uiModel, request);

        return "containerRegistration/show";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model uiModel, RegistrationRequest registrationRequest, HttpServletRequest request) {
        registrationRequest.setProviderId(loggedInUser(request).getExternalId());
        containerService.registerContainer(registrationRequest);
        out(WHPConstants.NOTIFICATION_MESSAGE, "Container registered successfully.", request);
        return "redirect:/containerRegistration";
    }

    private void populateWithInstances(Model uiModel, HttpServletRequest request) {
        ArrayList<String> instances = new ArrayList<>();
        for(Instance instance : Instance.values())
            instances.add(instance.getDisplayText());
        uiModel.addAttribute(INSTANCES, instances);

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotEmpty(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
    }
}

