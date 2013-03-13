package org.motechproject.whp.controller;

import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.service.ContainerRegistrationValidationPropertyValues;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.ContainerRegistrationValidator;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.flash.Flash.in;

public abstract class ContainerRegistrationController extends BaseWebController {

    public static final String INSTANCES = "instances";
    public static final String GENDERS = "genders";
    public static final String VALIDATION_PROPERTIES = "validationProperties";
    protected ContainerService containerService;
    protected ContainerRegistrationValidator containerRegistrationValidator;
    private ContainerRegistrationValidationPropertyValues containerRegistrationValidationPropertyValues;

    public ContainerRegistrationController(ContainerService containerService, ContainerRegistrationValidator containerRegistrationValidator, ContainerRegistrationValidationPropertyValues containerRegistrationValidationPropertyValues) {
        this.containerService = containerService;
        this.containerRegistrationValidator = containerRegistrationValidator;
        this.containerRegistrationValidationPropertyValues = containerRegistrationValidationPropertyValues;
    }

    protected boolean validate(Model uiModel, ContainerRegistrationRequest registrationRequest) {
        List<ErrorWithParameters> errors = containerRegistrationValidator.validate(registrationRequest);
        if (!errors.isEmpty()) {
            uiModel.addAttribute("errors", errors)
            .addAttribute("containerRegistrationRequest", registrationRequest);
            return true;
        }
        return false;
    }

    protected void populateViewDetails(Model uiModel, HttpServletRequest request) {
        if(!uiModel.containsAttribute("containerRegistrationRequest"))
            uiModel.addAttribute("containerRegistrationRequest", new ContainerRegistrationRequest());

        ArrayList<String> instances = new ArrayList<>();
        for (RegistrationInstance instance : RegistrationInstance.values())
            instances.add(instance.getDisplayText());
        uiModel.addAttribute(INSTANCES, instances);
        uiModel.addAttribute(GENDERS, Gender.values());
        uiModel.addAttribute(VALIDATION_PROPERTIES, containerRegistrationValidationPropertyValues);

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotBlank(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
    }

    protected void populateChannelId(ContainerRegistrationRequest registrationRequest) {
        registrationRequest.setChannelId(ChannelId.WEB.name());
    }

    protected void populateSubmitterDetails(ContainerRegistrationRequest registrationRequest, HttpServletRequest servletRequest) {
        MotechUser motechUser = loggedInUser(servletRequest);
        registrationRequest.setSubmitterId(motechUser.getUserName());
        registrationRequest.setSubmitterRole(getSupportedUserRole());
    }

    abstract String getSupportedUserRole();
}
