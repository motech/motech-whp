package org.motechproject.whp.controller;

import org.motechproject.export.annotation.Report;
import org.motechproject.export.annotation.ReportGroup;
import org.motechproject.flash.Flash;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
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
@ReportGroup(name = "adherence")
public class AdherenceController extends BaseController {

    private AllPatients allPatients;
    private WHPAdherenceService adherenceService;
    private AllTreatmentCategories allTreatmentCategories;

    @Autowired
    public AdherenceController(AllPatients allPatients, WHPAdherenceService adherenceService, AllTreatmentCategories allTreatmentCategories) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.allTreatmentCategories = allTreatmentCategories;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(patient);
        if (adherence == null) {
            adherence = WeeklyAdherence.createAdherenceFor(patient);
        }
        prepareModel(patient, uiModel, adherence);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId,
                         String categoryCode,
                         String remarks,
                         WeeklyAdherenceForm weeklyAdherenceForm,
                         HttpServletRequest httpServletRequest) {

        AuthenticatedUser authenticatedUser = loggedInUser(httpServletRequest);
        TreatmentCategory category = allTreatmentCategories.findByCode(categoryCode);

        AuditParams auditParams = new AuditParams(authenticatedUser.getUsername(), AdherenceSource.WEB, remarks);
        adherenceService.recordAdherence(patientId, weeklyAdherenceForm.weeklyAdherence(category), auditParams);
        Flash.out("message", "Adherence Saved For Patient : " + patientId, httpServletRequest);
        return "redirect:/";
    }

    @Report
    public List<Adherence> adherenceReport(int pageNumber) {
        return adherenceService.allAdherenceData(pageNumber - 1, 10000);
    }

    private void prepareModel(Patient patient, Model uiModel, WeeklyAdherence adherence) {
        TreatmentCategory category = allTreatmentCategories.findByCode(patient.latestTherapy().getTreatmentCategory().getCode());
        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(adherence, patient);

        uiModel.addAttribute("referenceDate", weeklyAdherenceForm.getReferenceDateString());
        uiModel.addAttribute("categoryCode", category.getCode());
        uiModel.addAttribute("adherence", weeklyAdherenceForm);
        uiModel.addAttribute("totalDoses", category.getDosesPerWeek());
        uiModel.addAttribute("readOnly", !(canUpdate(patient)));
    }
}