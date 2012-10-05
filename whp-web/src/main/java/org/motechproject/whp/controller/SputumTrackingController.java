package org.motechproject.whp.controller;

import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sputum-tracking")
public class SputumTrackingController {

    public static final String PRE_TREATMENT_VIEW = "sputum-tracking/pre-treatment";
    private ContainerDashboardService containerDashboardService;

    @Autowired
    public SputumTrackingController(ContainerDashboardService containerDashboardService) {
        this.containerDashboardService = containerDashboardService;
    }
}
