package org.motechproject.whp.controller;

import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/treatmentcard")
public class TreatmentCardController extends BaseController {

    AllPatients allPatients;

    TreatmentCardService treatmentCardService;

    WHPAdherenceService adherenceService;

    @Autowired
    public TreatmentCardController(WHPAdherenceService adherenceService, TreatmentCardService treatmentCardService, AllPatients allPatients) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.treatmentCardService = treatmentCardService;
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(patientId);
        uiModel.addAttribute("patientId", patient.getPatientId());
        uiModel.addAttribute("treatmentCard", treatmentCardService.treatmentCard(patient));
        return "treatmentcard/show";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@RequestBody UpdateAdherenceRequest updateAdherenceRequest, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(updateAdherenceRequest.getPatientId());
        adherenceService.addLogsForPatient(updateAdherenceRequest, patient);
        uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, "Treatment Card saved successfully");
        return show(patient.getPatientId(), uiModel, request);
    }

}
