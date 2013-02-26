package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.reporting.service.ReportingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;

@Controller
public class ProviderAdherenceStatusController extends BaseWebController {

    public static final String PROVIDER_ADHERENCE_STATUSES = "providerAdherenceStatuses";
    public static final String PROVIDED_ADHERENCE_FROM = "providedAdherenceFrom";
    public static final String PROVIDED_ADHERENCE_TO = "providedAdherenceTo";
    private ReportingDataService reportingDataService;


    @Autowired
    public ProviderAdherenceStatusController(ReportingDataService reportingDataService) {
        this.reportingDataService = reportingDataService;
    }

    @RequestMapping(value = "/providers/adherenceStatus", method = RequestMethod.GET)
    public String showAdherenceStatus(Model uiModel, HttpServletRequest request) {
        String loggedInDistrict = this.loggedInUser(request).getExternalId();
        LocalDate today = today();
        LocalDate treatmentWeekStartDate = week(today).startDate();
        LocalDate treatmentWeekEndDate = week(today).endDate();

        uiModel.addAttribute(PROVIDER_ADHERENCE_STATUSES, reportingDataService.getProviderAdherenceStatus(loggedInDistrict));
        uiModel.addAttribute(PROVIDED_ADHERENCE_FROM, WHPDate.date(treatmentWeekStartDate).value());
        uiModel.addAttribute(PROVIDED_ADHERENCE_TO, WHPDate.date(treatmentWeekEndDate).value());
        return "provider/adherence";
    }

    @RequestMapping(value = "/providers/adherenceStatus/print", method = RequestMethod.GET)
    public String printAdherenceStatus(Model uiModel, HttpServletRequest request) {
        showAdherenceStatus(uiModel, request);
        return "provider/printAdherence";
    }
}
