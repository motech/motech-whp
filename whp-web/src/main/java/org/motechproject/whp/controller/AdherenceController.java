package org.motechproject.whp.controller;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
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
public class AdherenceController {

    AllPatients allPatients;
    WHPAdherenceService adherenceService;

    @Autowired
    public AdherenceController(AllPatients allPatients, WHPAdherenceService adherenceService) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        prepareModel(patientId, uiModel);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Adherence adherence, Model uiModel) {
        prepareModel(patientId, uiModel);
        adherenceService.recordAdherence(patientId, adherence);
        return "patients";
    }

    private void prepareModel(String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
        uiModel.addAttribute("patientId", patientId);
        uiModel.addAttribute("adherence", new Adherence(DateUtil.today(), patient.getCurrentProvidedTreatment().getTreatment().getTreatmentCategory().getPillDays()));
    }

}
