package org.motechproject.whp.controller;

import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.contract.ContainerPatientDetailsRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.ReasonForClosureValidator;
import org.motechproject.whp.reponse.WHPResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ContainerTrackingController extends BaseWebController {
    protected ContainerService containerService;
    protected ReasonForClosureValidator reasonForClosureValidator;

    public ContainerTrackingController(ReasonForClosureValidator reasonForClosureValidator, ContainerService containerService) {
        this.reasonForClosureValidator = reasonForClosureValidator;
        this.containerService = containerService;
    }

    protected String openContainer(String containerId) {
        containerService.openContainer(containerId);
        return "success";
    }

    protected WHPResponse closeContainer(ContainerClosureRequest containerClosureRequest, HttpServletResponse httpServletResponse) {
        List<String> errors = reasonForClosureValidator.validate(containerClosureRequest);
        httpServletResponse.setContentType("application/json");
        if (!errors.isEmpty()) {
            httpServletResponse.setStatus(400);
            return new WHPResponse(errors);
        }

        containerService.closeContainer(containerClosureRequest);
        return new WHPResponse();
    }

    protected WHPResponse updateRegistrationPatientDetails(ContainerPatientDetailsRequest containerPatientDetailsRequest) {
        containerService.updatePatientDetails(containerPatientDetailsRequest);
        return new WHPResponse();
    }
}
