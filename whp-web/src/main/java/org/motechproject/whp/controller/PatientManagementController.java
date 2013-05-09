package org.motechproject.whp.controller;

import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/managepatients")
public class PatientManagementController extends BaseWebController {

    private PatientService patientService;

    @Autowired
    public PatientManagementController(PatientService patientService) {
        this.patientService = patientService;
    }

    @RequestMapping
    public String view(Model uiModel, HttpServletRequest request) {
        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotBlank(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
        return "patient/manage";
    }

    @RequestMapping(value = "treatment", method = RequestMethod.GET)
    public String findGivenTreatment(@RequestParam("patientId") String patientId,
                                     @RequestParam("tbId") String tbId,
                                     Model uiModel, HttpServletRequest request) {

        Patient patient = patientService.findByPatientId(patientId);
        if (patient!=null)
            uiModel.addAttribute("treatment", patient.getTreatmentBy(tbId));
        return "patient/treatmentDetails";
    }

    @RequestMapping(value = "removeTreatment", method = RequestMethod.POST)
    public String removeTreatment(@RequestParam("patientId") String patientId,
                                     @RequestParam("tbId") String tbId,
                                     HttpServletRequest request) {
        Patient patient = patientService.findByPatientId(patientId);

        if (patient.canRemoveTreatment(tbId)){
            patient.removeTreatmentForTbId(tbId);
            patientService.update(patient);
            out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Treatment with id %s removed successfully.", tbId), request);
        }
        else
            out(WHPConstants.NOTIFICATION_MESSAGE, String.format("Treatment with id %s cannot be removed.", tbId), request);


        return "redirect:/managepatients";
    }
}
