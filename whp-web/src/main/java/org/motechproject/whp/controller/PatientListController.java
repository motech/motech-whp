package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.model.AlertDateFilters;
import org.motechproject.whp.patient.model.AlertTypeFilters;
import org.motechproject.whp.patient.model.FlagFilters;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.uimodel.PatientDashboardLegends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/patients")
public class PatientListController extends BaseWebController {

    public static final String DISTRICT_LIST = "districts";

    private AllDistricts allDistrictsCache;
    private AllTreatmentCategories allTreatmentCategories;
    private TreatmentWeekInstance treatmentWeekInstance;
    private PatientDashboardLegends patientDashboardLegends;
    private AlertTypeFilters alertTypeFilters;
    private AlertDateFilters alertDateFilters;

    @Autowired
    public PatientListController(AllDistricts allDistrictsCache,
                                 AllTreatmentCategories allTreatmentCategories,
                                 TreatmentWeekInstance treatmentWeekInstance,
                                 PatientDashboardLegends patientDashboardLegends,
                                 AlertTypeFilters alertTypeFilters, AlertDateFilters alertDateFilters) {

        this.allDistrictsCache = allDistrictsCache;
        this.allTreatmentCategories = allTreatmentCategories;
        this.treatmentWeekInstance = treatmentWeekInstance;
        this.patientDashboardLegends = patientDashboardLegends;
        this.alertTypeFilters = alertTypeFilters;
        this.alertDateFilters = alertDateFilters;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model uiModel) {
        prepareModelForListView(uiModel);
        return "patient/list";
    }

    private void prepareModelForListView(Model uiModel) {
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute("alertTypes", alertTypeFilters);
        uiModel.addAttribute("alertDates", alertDateFilters);
        uiModel.addAttribute("flags", new FlagFilters());
        uiModel.addAttribute("legends", patientDashboardLegends.getLegends());
        uiModel.addAttribute("treatmentCategories", allTreatmentCategories.getAll());
        uiModel.addAttribute("lastSunday", WHPDate.date(treatmentWeekInstance.previousAdherenceWeekEndDate()).lucidValue());
    }
}
