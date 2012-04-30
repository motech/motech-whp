package org.motechproject.whp.controller;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/adherence")
public class WeeklyAdherenceController {

    public static final String PATIENT = "patient";

    AllPatients allPatients;

    @Autowired
    public WeeklyAdherenceController(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
        uiModel.addAttribute(PATIENT, patient);
        return "adherence/update";
    }

}
