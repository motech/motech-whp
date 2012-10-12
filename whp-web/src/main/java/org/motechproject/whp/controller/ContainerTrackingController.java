package org.motechproject.whp.controller;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.tracking.service.ContainerTrackingService;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.motechproject.whp.request.UpdateReasonForClosureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value="/sputum-tracking")
public class ContainerTrackingController {


    private ContainerService containerService;
    private ContainerTrackingService containerTrackingService;

    public static final String REASONS = "reasons";
    public static final String ALTERNATE_DIAGNOSIS_LIST = "alternateDiagnosisList";

    @Autowired
    public ContainerTrackingController(ContainerService containerService, ContainerTrackingService containerTrackingService) {
        this.containerService = containerService;
        this.containerTrackingService = containerTrackingService;
    }

    @RequestMapping(value = "/reasonForClosure",method = RequestMethod.POST)
    public String updateReasonForClosure(UpdateReasonForClosureRequest updateReasonForClosureRequest){

        String containerId = updateReasonForClosureRequest.getContainerId();

        Container container = containerService.getContainer(containerId);
        container.setReasonForClosure(updateReasonForClosureRequest.getReason());
        containerService.update(container);

        return "redirect:/sputum-tracking/pre-treatment";
    }

    @RequestMapping(value="/pre-treatment", method = RequestMethod.GET)
    public String showContainerTrackingDashBoard(Model uiModel){
        List<ReasonForContainerClosure> allClosureReasons = containerTrackingService.getAllClosureReasons();
        List<AlternateDiagnosisList> allAlternateDiagnosisList = containerTrackingService.getAllAlternateDiagnosisList();

        uiModel.addAttribute(REASONS, allClosureReasons);
        uiModel.addAttribute(ALTERNATE_DIAGNOSIS_LIST, allAlternateDiagnosisList);

        return "sputum-tracking/pre-treatment";
    }
}
