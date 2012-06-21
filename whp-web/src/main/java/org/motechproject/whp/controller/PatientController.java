package org.motechproject.whp.controller;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.flash.Flash;
import org.motechproject.whp.uimodel.UpdateAdherenceRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.service.TreatmentCardService;
import org.motechproject.whp.uimodel.PatientDTO;
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
public class PatientController  extends BaseController{

    public static final String PATIENT_LIST = "patientList";
    AllPatients allPatients;
    AllAdherenceLogs allAdherenceLogs;
    TreatmentCardService treatmentCardService;
    public static final String NOTIFICATION_MESSAGE = "message";

    @Autowired
    public PatientController(AllPatients allPatients, AllAdherenceLogs allAdherenceLogs, TreatmentCardService treatmentCardService) {
        this.allPatients = allPatients;
        this.allAdherenceLogs = allAdherenceLogs;
        this.treatmentCardService = treatmentCardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listByProvider(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = allPatients.getAllWithActiveTreatmentFor(providerId);
        passPatientsAsModelToListView(uiModel, patientsForProvider);
        passAdherenceSavedMessageToListView(uiModel, request);

        return "patient/listByProvider";
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public String list(Model uiModel) {
        List<Patient> patients = allPatients.getAllWithActiveTreatment();
        passPatientsAsModelToListView(uiModel, patients);
        return "patient/list";
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

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(patientId);
        setupModelForDashboard(uiModel, request, patient);
        return "patient/show";
    }

    private void setupModelForDashboard(Model uiModel, HttpServletRequest request, Patient patient) {
        PatientDTO patientDTO = new PatientDTO(patient);
        uiModel.addAttribute("patient", patientDTO);
        uiModel.addAttribute("patientId", patient.getPatientId());
        List<String> messages = FlashUtil.flashAllIn("dateUpdatedMessage", request);
        if (CollectionUtils.isNotEmpty(messages)) {
            uiModel.addAttribute("messages", messages);
        }
        uiModel.addAttribute("treatmentCard", treatmentCardService.getIntensivePhaseTreatmentCardModel(patient));
    }

    @RequestMapping(value = "saveTreatmentCard", method = RequestMethod.POST)
    public String saveTreatmentCard(@RequestParam("delta") String delta, Model uiModel, HttpServletRequest request) {
        UpdateAdherenceRequest updateAherenceRequest = new Gson().fromJson(delta, UpdateAdherenceRequest.class);
        Patient patient = allPatients.findByPatientId(updateAherenceRequest.getPatientId());
        treatmentCardService.addLogsForPatient(updateAherenceRequest, patient);
        uiModel.addAttribute(NOTIFICATION_MESSAGE, "Treatment Card saved successfully");
        return show(patient.getPatientId(), uiModel, request);
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.POST)
    public String update(@RequestParam("patientId") String patientId, PatientDTO patientDTO, HttpServletRequest httpServletRequest) {
        Patient updatedPatient = patientDTO.mapNewPhaseInfoToPatient(allPatients.findByPatientId(patientId));
        allPatients.update(updatedPatient);
        //TODO: move flashing actions to a service
        flashOutDateUpdatedMessage(patientId, patientDTO, httpServletRequest);
        return String.format("redirect:/patients/dashboard?patientId=%s", patientId);
    }

    private void flashOutDateUpdatedMessage(String patientId, PatientDTO patientDTO, HttpServletRequest httpServletRequest) {
        String ipStartDate = patientDTO.getIpStartDate();
        String eipStartDate = patientDTO.getEipStartDate();
        String cpStartDate = patientDTO.getCpStartDate();

        List<String> messages = new ArrayList<String>();

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
