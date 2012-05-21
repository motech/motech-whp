package org.motechproject.whp.controller;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.motechproject.flash.FlashAttributeName.in;

@Controller
@RequestMapping(value = "/patients")
public class PatientController {

    public static final String PATIENT_LIST = "patientList";
    public static final String MESSAGE = "message";

    AllPatients allPatients;

    @Autowired
    public PatientController(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = allPatients.getAllWithActiveTreatment(providerId);
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);

        Object message = request.getAttribute(in("message"));
        if (null != message) {
            uiModel.addAttribute("message", message.toString());
        }
        return "patient/list";
    }
}
