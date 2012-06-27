package org.motechproject.whp.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.flash.Flash;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.util.FlashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Controller
@RequestMapping(value = "/patients")
public class PatientController extends BaseController {

    public static final String PATIENT_LIST = "patientList";
    AllPatients allPatients;
    AllAdherenceLogs allAdherenceLogs;
    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    @Autowired
    public PatientController(AllPatients allPatients, AllAdherenceLogs allAdherenceLogs, PhaseUpdateOrchestrator phaseUpdateOrchestrator) {
        this.allPatients = allPatients;
        this.allAdherenceLogs = allAdherenceLogs;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listByProvider(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = allPatients.getAllWithActiveTreatmentFor(providerId);
        passPatientsAsModelToListView(uiModel, patientsForProvider);
        passAdherenceSavedMessageToListView(uiModel, request);
        return "patient/listByProvider";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model uiModel) {
        List<Patient> patients = allPatients.getAllWithActiveTreatment();
        passPatientsAsModelToListView(uiModel, patients);
        return "patient/list";
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(patientId);
        setupModel(uiModel, request, patient);
        return "patient/show";
    }

    @RequestMapping(value = "adjustPhaseStartDates", method = RequestMethod.POST)
    public String adjustPhaseStartDates(@RequestParam("patientId") String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        Patient updatedPatient = phaseStartDates.mapNewPhaseInfoToPatient(allPatients.findByPatientId(patientId));
        allPatients.update(updatedPatient);
        phaseUpdateOrchestrator.recomputePillCount(updatedPatient.getPatientId());
        flashOutDateUpdatedMessage(patientId, phaseStartDates, httpServletRequest);
        return String.format("redirect:/patients/show?patientId=%s", patientId);
    }


    private void passAdherenceSavedMessageToListView(Model uiModel, HttpServletRequest request) {
        String message = Flash.in("message", request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute("message", message);
        }
    }

    private void passPatientsAsModelToListView(Model uiModel, List<Patient> patientsForProvider) {
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);
    }

    private void setupModel(Model uiModel, HttpServletRequest request, Patient patient) {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        uiModel.addAttribute("patient", patient);
        uiModel.addAttribute("phaseStartDates", phaseStartDates);
        List<String> messages = FlashUtil.flashAllIn("dateUpdatedMessage", request);
        if (CollectionUtils.isNotEmpty(messages)) {
            uiModel.addAttribute("messages", messages);
        }
    }



    private void flashOutDateUpdatedMessage(String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        //TODO: Use templates and externalize messages
        String ipStartDate = phaseStartDates.getIpStartDate();
        String eipStartDate = phaseStartDates.getEipStartDate();
        String cpStartDate = phaseStartDates.getCpStartDate();

        List<String> messages = new ArrayList<>();

        String message1 = "Dates Updated For patient: " + patientId;

        String message2 = "Intensive Phase Start Date: ";
        message2 = message2.concat(StringUtils.isBlank(ipStartDate) ? "Not Set" : ipStartDate);

        String message3 = "Extended Intensive Phase Start Date: ";
        message3 = message3.concat(StringUtils.isBlank(eipStartDate) ? "Not Set" : eipStartDate);

        String message4 = "Continuation Phase Start Date: ";
        message4 = message4.concat(StringUtils.isBlank(cpStartDate) ? "Not Set" : cpStartDate);

        messages.addAll(asList(message1, message2, message3, message4));

        FlashUtil.flashAllOut("dateUpdatedMessage", messages, httpServletRequest);
    }
}
