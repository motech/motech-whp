package org.motechproject.whp.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/patients")
public class PatientController {

    public static final String PATIENT_LIST = "patientList";
    AllPatients allPatients;
    PatientService patientService;

    @Autowired
    public PatientController(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listByProvider(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = allPatients.getAllWithActiveTreatmentFor(providerId);
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);
        String message = Flash.in("message", request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute("message", message);
        }
        return "patient/listByProvider";
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(patientId);
        PatientDTO patientDTO = new PatientDTO(patient);
        uiModel.addAttribute("patient", patientDTO);
        uiModel.addAttribute("patientId", patientId);
        List<String> messages = FlashUtil.flashAllIn("dateUpdatedMessage", request);
        if (CollectionUtils.isNotEmpty(messages)) {
            uiModel.addAttribute("messages", messages);
        }
        return "patient/show";
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

        messages.addAll(Arrays.asList(message1, message2, message3, message4));

        FlashUtil.flashAllOut("dateUpdatedMessage", messages, httpServletRequest);
    }
}
