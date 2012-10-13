package org.motechproject.whp.controller;

import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria.canUpdate;

@Controller
@RequestMapping(value = "/adherence")
public class AdherenceController extends BaseWebController {

    private PatientService patientService;
    private WHPAdherenceService adherenceService;
    private AllTreatmentCategories allTreatmentCategories;
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    @Autowired
    public AdherenceController(PatientService patientService, WHPAdherenceService adherenceService, AllTreatmentCategories allTreatmentCategories, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator) {
        this.patientService = patientService;
        this.adherenceService = adherenceService;
        this.allTreatmentCategories = allTreatmentCategories;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        Patient patient = patientService.findByPatientId(patientId);
        WeeklyAdherenceSummary adherenceSummary = adherenceService.currentWeekAdherence(patient);
        prepareModel(patient, uiModel, adherenceSummary);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId,
                         String remarks,
                         WeeklyAdherenceForm weeklyAdherenceForm,
                         HttpServletRequest httpServletRequest) {

        MotechUser authenticatedUser = loggedInUser(httpServletRequest);

        AuditParams auditParams = new AuditParams(authenticatedUser.getUserName(), AdherenceSource.WEB, remarks);

        treatmentUpdateOrchestrator.recordWeeklyAdherence(weeklyAdherenceSummary(weeklyAdherenceForm), patientId, auditParams);

        Flash.out(WHPConstants.NOTIFICATION_MESSAGE, "Adherence Saved For Patient : " + patientId, httpServletRequest);
        return "redirect:/";
    }

    private WeeklyAdherenceSummary weeklyAdherenceSummary(WeeklyAdherenceForm weeklyAdherenceForm) {
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(weeklyAdherenceForm.getPatientId(), new TreatmentWeek(weeklyAdherenceForm.getReferenceDate()));
        weeklyAdherenceSummary.setDosesTaken(weeklyAdherenceForm.getNumberOfDosesTaken());
        return weeklyAdherenceSummary;
    }

    private void prepareModel(Patient patient, Model uiModel, WeeklyAdherenceSummary adherenceSummary) {
        TreatmentCategory category = allTreatmentCategories.findByCode(patient.getCurrentTherapy().getTreatmentCategory().getCode());
        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(adherenceSummary, patient);

        uiModel.addAttribute("adherence", weeklyAdherenceForm);
        uiModel.addAttribute("totalDoses", category.getDosesPerWeek());
        uiModel.addAttribute("readOnly", !(canUpdate(patient)));
        uiModel.addAttribute("weekStartDate", WHPDate.date(TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate()).value());
        uiModel.addAttribute("weekEndDate", WHPDate.date(TreatmentWeekInstance.currentAdherenceCaptureWeek().endDate()).value());
    }
}