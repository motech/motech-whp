package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.ReasonForClosureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/sputum-tracking/in-treatment/")
public class InTreatmentContainerTrackingController {

    public static final String CONTAINER_STATUS_LIST = "containerStatusList";
    private ContainerService containerService;
    private ReasonForClosureValidator reasonForClosureValidator;
    private AllDistricts allDistricts;

    public static final String REASONS = "reasons";
    public static final String ERRORS = "errors";
    public static final String LAB_RESULTS = "labResults";
    public static final String DISTRICTS = "districts";
    public static final String INSTANCES = "instances";

    @Autowired
    public InTreatmentContainerTrackingController(ContainerService containerService, ReasonForClosureValidator reasonForClosureValidator, AllDistricts allDistricts) {
        this.containerService = containerService;
        this.reasonForClosureValidator = reasonForClosureValidator;
        this.allDistricts = allDistricts;
    }

    @RequestMapping(value = "/close-container", method = RequestMethod.POST)
    public String updateReasonForClosure(ContainerClosureRequest containerClosureRequest) {
        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);
        if (!errors.isEmpty())
            return "error";

        containerService.closeContainer(containerClosureRequest);
        return "success";
    }

    @RequestMapping(value = "/open-container", method = RequestMethod.GET)
    public String openContainer(@RequestParam("containerId") String containerId) {
        containerService.openContainer(containerId);
        return "success";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showContainerTrackingDashBoard(Model uiModel) {
        List<ReasonForContainerClosure> allClosureReasons = containerService.getAllInTreatmentClosureReasonsForAdmin();

        uiModel.addAttribute(INSTANCES, SputumTrackingInstance.allInTreatmentInstanceNames());
        uiModel.addAttribute(REASONS, allClosureReasons);
        uiModel.addAttribute(LAB_RESULTS, SmearTestResult.allNames());
        uiModel.addAttribute(CONTAINER_STATUS_LIST, ContainerStatus.allNames());
        uiModel.addAttribute(DISTRICTS, allDistricts.getAll());

        return "sputumTracking/inTreatmentDashboard";
    }
}
