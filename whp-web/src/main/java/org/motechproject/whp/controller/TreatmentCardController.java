package org.motechproject.whp.controller;

import com.google.gson.Gson;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
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
public class TreatmentCardController extends BaseWebController {

    AllPatients allPatients;

    TreatmentCardService treatmentCardService;


    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    @Autowired
    public TreatmentCardController(TreatmentCardService treatmentCardService, AllPatients allPatients, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator) {
        this.allPatients = allPatients;
        this.treatmentCardService = treatmentCardService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
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
        MotechUser authenticatedUser = loggedInUser(request);
        AuditParams auditParams = new AuditParams(authenticatedUser.getUserName(), AdherenceSource.WEB, null);

        UpdateAdherenceRequest updateAdherenceRequest = new Gson().fromJson(adherenceJson, UpdateAdherenceRequest.class);
        Patient patient = allPatients.findByPatientId(updateAdherenceRequest.getPatientId());
        treatmentUpdateOrchestrator.recordDailyAdherence(updateAdherenceRequest.getDailyAdherenceRequests(), patient, auditParams);

        out(WHPConstants.NOTIFICATION_MESSAGE, "Treatment Card saved successfully", request);
        return redirectToPatientDashboardURL(patient.getPatientId());
    }

}
