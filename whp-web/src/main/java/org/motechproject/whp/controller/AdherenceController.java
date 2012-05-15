package org.motechproject.whp.controller;

import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportData;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.motechproject.whp.criteria.UpdateAdherenceCriteria.canUpdate;

@Controller
@RequestMapping(value = "/adherence")
@Report(name = "adherenceReport")
public class AdherenceController extends BaseController {

    private AllPatients allPatients;
    private WHPAdherenceService adherenceService;

    @Autowired
    public AdherenceController(AllPatients allPatients, WHPAdherenceService adherenceService) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(patient);
        prepareModel(patient, uiModel, adherence);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, WeeklyAdherenceForm weeklyAdherenceForm, HttpServletRequest httpServletRequest) {
        AuthenticatedUser authenticatedUser = loggedInUser(httpServletRequest);
        adherenceService.recordAdherence(patientId, weeklyAdherenceForm.weeklyAdherence(), authenticatedUser.getUsername(), AdherenceSource.WEB);
        return "forward:/";
    }

    @ReportData
    public List<Adherence> adherenceReportData(int pageNumber) {
        return adherenceService.allAdherenceData(pageNumber - 1, 10000);
    }

    private void prepareModel(Patient patient, Model uiModel, WeeklyAdherence adherence) {
        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(adherence, patient.getTreatmentInterruptions());
        uiModel.addAttribute("referenceDate", weeklyAdherenceForm.getReferenceDateString());
        uiModel.addAttribute("adherence", weeklyAdherenceForm);
        uiModel.addAttribute("readOnly", !(canUpdate(patient)));
    }

}
