package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.ReasonsForClosureService;
import org.motechproject.whp.container.validation.ReasonForClosureValidator;
import org.motechproject.whp.reponse.WHPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/sputum-tracking/pre-treatment/")
public class PreTreatmentContainerTrackingController extends ContainerTrackingController {


    private AllDistricts allDistricts;
    private ReasonsForClosureService reasonsForClosureService;

    public static final String CONTAINER_STATUS_LIST = "containerStatusList";
    public static final String DIAGNOSIS_LIST = "diagnosisList";

    public static final String REASONS = "reasons";
    public static final String ALTERNATE_DIAGNOSIS_LIST = "alternateDiagnosisList";
    public static final String LAB_RESULTS = "labResults";
    public static final String DISTRICTS = "districts";
    public static final String REASONS_FOR_FILTER = "reasonsForFilter";

    @Autowired
    public PreTreatmentContainerTrackingController(ContainerService containerService, AllDistricts allDistricts, ReasonForClosureValidator reasonForClosureValidator, ReasonsForClosureService reasonsForClosureService) {
        super(reasonForClosureValidator, containerService);
        this.allDistricts = allDistricts;
        this.reasonsForClosureService = reasonsForClosureService;
    }

    @RequestMapping(value = "/close-container", method = RequestMethod.POST)
    @ResponseBody
    public WHPResponse closePreTreatmentContainer(ContainerClosureRequest containerClosureRequest, HttpServletResponse httpServletResponse) {
        return closeContainer(containerClosureRequest, httpServletResponse);
    }

    @RequestMapping(value = "/open-container", method = RequestMethod.GET)
    @ResponseBody
    public String openPreTreatmentContainer(@RequestParam("containerId") String containerId) {
        return openContainer(containerId);
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showContainerTrackingDashBoard(Model uiModel) {
        List<ReasonForContainerClosure> allClosureReasons = reasonsForClosureService.getAllPreTreatmentClosureReasonsForAdmin();
        List<AlternateDiagnosis> allAlternateDiagnosis = containerService.getAllAlternateDiagnosis();

        uiModel.addAttribute(REASONS, allClosureReasons);
        uiModel.addAttribute(REASONS_FOR_FILTER, reasonsForClosureService.getAllReasonsPreTreatmentClosureReasons());
        uiModel.addAttribute(ALTERNATE_DIAGNOSIS_LIST, allAlternateDiagnosis);
        uiModel.addAttribute(LAB_RESULTS, SmearTestResult.allNames());
        uiModel.addAttribute(CONTAINER_STATUS_LIST, ContainerStatus.allNames());
        uiModel.addAttribute(DIAGNOSIS_LIST, Diagnosis.allNames());
        uiModel.addAttribute(DISTRICTS, allDistricts.getAll());

        return "sputumTracking/preTreatmentDashboard";
    }
}
