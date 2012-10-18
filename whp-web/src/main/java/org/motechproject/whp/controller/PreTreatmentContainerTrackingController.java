package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.validation.ReasonForClosureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/sputum-tracking/pre-treatment/")
public class PreTreatmentContainerTrackingController {


    public static final String CONTAINER_STATUS_LIST = "containerStatusList";
    public static final String DIAGNOSIS_LIST = "diagnosisList";
    private ContainerService containerService;
    private ReasonForClosureValidator reasonForClosureValidator;
    private AllDistricts allDistricts;

    public static final String REASONS = "reasons";
    public static final String ALTERNATE_DIAGNOSIS_LIST = "alternateDiagnosisList";
    public static final String ERRORS = "errors";
    public static final String LAB_RESULTS = "labResults";
    public static final String DISTRICTS = "districts";

    @Autowired
    public PreTreatmentContainerTrackingController(ContainerService containerService, ReasonForClosureValidator reasonForClosureValidator, AllDistricts allDistricts) {
        this.containerService = containerService;
        this.reasonForClosureValidator = reasonForClosureValidator;
        this.allDistricts = allDistricts;
    }

    @RequestMapping(value = "/close-container", method = RequestMethod.POST)
    @ResponseBody
    public String updateReasonForClosure(ContainerClosureRequest containerClosureRequest) {
        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);
        if (!errors.isEmpty())
            return "error";

        containerService.closeContainer(containerClosureRequest);
        return "success";
    }

    @RequestMapping(value = "/open-container", method = RequestMethod.GET)
    @ResponseBody
    public String openContainer(@RequestParam("containerId") String containerId) {
        containerService.openContainer(containerId);

        return "success";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showContainerTrackingDashBoard(Model uiModel) {
        List<ReasonForContainerClosure> allClosureReasons = containerService.getAllPreTreatmentClosureReasonsForAdmin();
        List<AlternateDiagnosis> allAlternateDiagnosis = containerService.getAllAlternateDiagnosis();

        uiModel.addAttribute(REASONS, allClosureReasons);
        uiModel.addAttribute(ALTERNATE_DIAGNOSIS_LIST, allAlternateDiagnosis);
        uiModel.addAttribute(LAB_RESULTS, SmearTestResult.allNames());
        uiModel.addAttribute(CONTAINER_STATUS_LIST, ContainerStatus.allNames());
        uiModel.addAttribute(DIAGNOSIS_LIST, Diagnosis.allNames());
        uiModel.addAttribute(DISTRICTS, allDistricts.getAll());

        return "sputumTracking/preTreatmentDashboard";
    }
}
