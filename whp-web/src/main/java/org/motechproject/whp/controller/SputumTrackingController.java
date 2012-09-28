package org.motechproject.whp.controller;

import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sputumTracking")
public class SputumTrackingController {

    public static final String PRE_TREATMENT_VIEW = "sputumTracking/preTreatment";
    private ContainerDashboardService containerDashboardService;

    @Autowired
    public SputumTrackingController(ContainerDashboardService containerDashboardService) {
        this.containerDashboardService = containerDashboardService;
    }

    @RequestMapping(value = "preTreatment", method = RequestMethod.GET)
    public String preTreatmentTracking(Model uiModel, HttpServletRequest request) {
        List<ContainerDashboardRow> containerDashboardRows = containerDashboardService.allContainerDashboardRows();
        uiModel.addAttribute("sputumContainers",containerDashboardRows);
        return PRE_TREATMENT_VIEW;
    }
}
