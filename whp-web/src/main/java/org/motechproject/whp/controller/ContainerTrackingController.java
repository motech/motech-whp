package org.motechproject.whp.controller;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.request.UpdateReasonForClosureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/sputum-tracking")
public class ContainerTrackingController {


    private ContainerService containerService;

    @Autowired
    public ContainerTrackingController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @RequestMapping(value = "/reasonForClosure",method = RequestMethod.POST)
    public String updateReasonForClosure(UpdateReasonForClosureRequest updateReasonForClosureRequest){

        String containerId = updateReasonForClosureRequest.getContainerId();

        Container container = containerService.getContainer(containerId);
        container.setReasonForClosure(updateReasonForClosureRequest.getReason());
        containerService.update(container);

        return "redirect:/sputum-tracking/pre-treatment";
    }

/*
    @RequestMapping(value="/pre-treatment")
    public String showContainerTrackingDashBoard(Model uiModel){
        uiModel.addReason()
        return "/sputum-tracking/pre-treatment";
    }
*/

}
