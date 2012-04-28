package org.motechproject.whp.controller;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/providers")
public class ProviderController {

    public static final String PATIENT_LIST = "patientList";

    AllPatients allPatients;

    @Autowired
    public ProviderController(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String list(@PathVariable("id") String providerId, Model uiModel) {
        List<Patient> patientsForProvider = allPatients.findByCurrentProviderId(providerId);
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);
        return "provider";
    }
}
