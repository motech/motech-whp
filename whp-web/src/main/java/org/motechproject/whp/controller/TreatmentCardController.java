package org.motechproject.whp.controller;

import com.google.gson.Gson;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static org.motechproject.flash.Flash.out;
import static org.motechproject.whp.controller.PatientController.redirectToPatientDashboardURL;

@Controller
@RequestMapping(value = "/treatmentcard")
public class TreatmentCardController extends BaseController {

    AllPatients allPatients;

    TreatmentCardService treatmentCardService;

    WHPAdherenceService adherenceService;

    PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    @Autowired
    public TreatmentCardController(WHPAdherenceService adherenceService, TreatmentCardService treatmentCardService, AllPatients allPatients, PhaseUpdateOrchestrator phaseUpdateOrchestrator) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.treatmentCardService = treatmentCardService;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
        uiModel.addAttribute("patientId", patient.getPatientId());
        uiModel.addAttribute("treatmentCard", treatmentCardService.treatmentCard(patient));
        return "treatmentcard/show";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@RequestParam("delta") String adherenceJson, HttpServletRequest request) {
        UpdateAdherenceRequest updateAdherenceRequest = new Gson().fromJson(adherenceJson, UpdateAdherenceRequest.class);
        Patient patient = allPatients.findByPatientId(updateAdherenceRequest.getPatientId());
        adherenceService.addLogsForPatient(updateAdherenceRequest, patient);
        phaseUpdateOrchestrator.recomputePillStatus(updateAdherenceRequest.getPatientId());
        phaseUpdateOrchestrator.attemptPhaseTransition(updateAdherenceRequest.getPatientId());
        out(WHPConstants.NOTIFICATION_MESSAGE, "Treatment Card saved successfully", request);
        return redirectToPatientDashboardURL(patient.getPatientId());
    }

}
