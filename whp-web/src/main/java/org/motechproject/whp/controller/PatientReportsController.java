package org.motechproject.whp.controller;

import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PatientReportsController {

    private AllDistricts allDistricts;

    @Autowired
    public PatientReportsController(AllDistricts allDistricts) {
        this.allDistricts = allDistricts;
    }

    @RequestMapping("/patientreports/filter")
    public String filter(Model uiModel){
        uiModel.addAttribute("districts", allDistricts.getAll());
        return "patientreports/filter";
    }
}
