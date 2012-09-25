package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.RegistrationRequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.flash.Flash.in;

public abstract class ContainerRegistrationController extends BaseWebController {

    public static final String INSTANCES = "instances";
    public static final String CONTAINER_ID_MAX_LENGTH = "containerIdMaxLength";
    protected ContainerService containerService;
    protected RegistrationRequestValidator registrationRequestValidator;
    protected SputumTrackingProperties sputumTrackingProperties;

    public ContainerRegistrationController(ContainerService containerService, RegistrationRequestValidator registrationRequestValidator, SputumTrackingProperties sputumTrackingProperties) {
        this.containerService = containerService;
        this.registrationRequestValidator = registrationRequestValidator;
        this.sputumTrackingProperties = sputumTrackingProperties;
    }

    protected boolean hasErrors(Model uiModel, ContainerRegistrationRequest registrationRequest, HttpServletRequest request) {
        List<String> errors = registrationRequestValidator.validate(registrationRequest);
        if (!errors.isEmpty()) {
            uiModel.addAttribute("errors", StringUtils.join(errors, ","));
            return true;
        }
        return false;
    }

    protected void populateViewDetails(Model uiModel, HttpServletRequest request) {
        ArrayList<String> instances = new ArrayList<>();
        for (SputumTrackingInstance sputumTrackingInstance : SputumTrackingInstance.values())
            instances.add(sputumTrackingInstance.getDisplayText());
        uiModel.addAttribute(INSTANCES, instances);

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotBlank(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
        uiModel.addAttribute(CONTAINER_ID_MAX_LENGTH, sputumTrackingProperties.getContainerIdMaxLength());
    }
}