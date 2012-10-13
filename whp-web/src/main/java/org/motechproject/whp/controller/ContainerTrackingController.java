package org.motechproject.whp.controller;

import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.tracking.validation.ReasonForClosureValidator;
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
    private ReasonForClosureValidator reasonForClosureValidator;

    public static final String REASONS = "reasons";
    public static final String ALTERNATE_DIAGNOSIS_LIST = "alternateDiagnosisList";
    public static final String ERRORS = "errors";

    @Autowired
    public ContainerTrackingController(ContainerService containerService, ReasonForClosureValidator reasonForClosureValidator) {
        this.containerService = containerService;
        this.reasonForClosureValidator = reasonForClosureValidator;
    }

    @RequestMapping(value = "/close-container",method = RequestMethod.POST)
    public String updateReasonForClosure(Model uiModel, ContainerClosureRequest containerClosureRequest){
        List<ErrorWithParameters> errors = reasonForClosureValidator.validate(containerClosureRequest);
        if(!errors.isEmpty()) {
            uiModel.addAttribute(ERRORS, errors);
            return showContainerTrackingDashBoard(uiModel);
        }

        containerService.closeContainer(containerClosureRequest);
        return "redirect:/sputum-tracking/pre-treatment";
    }

    @RequestMapping(value="/pre-treatment", method = RequestMethod.GET)
    public String showContainerTrackingDashBoard(Model uiModel){
        List<ReasonForContainerClosure> allClosureReasons = containerService.getAllClosureReasonsForAdmin();
        List<AlternateDiagnosis> allAlternateDiagnosis = containerService.getAllAlternateDiagnosis();

        uiModel.addAttribute(REASONS, allClosureReasons);
        uiModel.addAttribute(ALTERNATE_DIAGNOSIS_LIST, allAlternateDiagnosis);

        return "sputum-tracking/pre-treatment";
    }
}
