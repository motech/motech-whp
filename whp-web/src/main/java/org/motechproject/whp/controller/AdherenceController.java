package org.motechproject.whp.controller;

import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.criteria.UpdateAdherenceCriteria;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.AdherenceForm;
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
    UpdateAdherenceCriteria adherenceCriteria;

    @Autowired
    public AdherenceController(AllPatients allPatients, WHPAdherenceService adherenceService, UpdateAdherenceCriteria adherenceCriteria) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.adherenceCriteria = adherenceCriteria;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(patientId);
        prepareModel(patientId, uiModel, adherence);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, AdherenceForm adherenceForm) {
        adherenceService.recordAdherence(patientId, adherenceForm.weeklyAdherence());
        return "forward:/";
    }

    private void prepareModel(String patientId, Model uiModel, WeeklyAdherence adherence) {
        AdherenceForm adherenceForm = new AdherenceForm(adherence);
        uiModel.addAttribute("patientId", patientId);
        uiModel.addAttribute("adherence", adherenceForm);
        uiModel.addAttribute("referenceDate", adherenceForm.getReferenceDateString());
        uiModel.addAttribute("readOnly", !(adherenceCriteria.canUpdate(patientId)));
    }

}
