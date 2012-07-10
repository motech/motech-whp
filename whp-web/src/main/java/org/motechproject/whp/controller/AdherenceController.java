package org.motechproject.whp.controller;

import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;
import org.motechproject.flash.Flash;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.common.TreatmentWeekInstance;
import org.motechproject.whp.common.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.repository.AllTreatmentCategories;
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
@ExcelDataSource(name = "adherence")
public class AdherenceController extends BaseController {

    private AllPatients allPatients;
    private WHPAdherenceService adherenceService;
    private AllTreatmentCategories allTreatmentCategories;
    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    @Autowired
    public AdherenceController(AllPatients allPatients, WHPAdherenceService adherenceService, AllTreatmentCategories allTreatmentCategories, PhaseUpdateOrchestrator phaseUpdateOrchestrator) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.allTreatmentCategories = allTreatmentCategories;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        Patient patient = allPatients.findByPatientId(patientId);
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
        adherenceService.recordAdherence(weeklyAdherenceSummary(weeklyAdherenceForm), auditParams);
        phaseUpdateOrchestrator.recomputePillCount(patientId);
        phaseUpdateOrchestrator.attemptPhaseTransition(patientId);
        Flash.out(WHPConstants.NOTIFICATION_MESSAGE, "Adherence Saved For Patient : " + patientId, httpServletRequest);
        return "redirect:/";
    }

    @DataProvider
    public List<Adherence> adherenceReport(int pageNumber) {
        return adherenceService.allAdherenceData(pageNumber - 1, 10000);
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
        uiModel.addAttribute("weekStartDate", WHPDate.date(TreatmentWeekInstance.currentWeekInstance().startDate()).value());
        uiModel.addAttribute("weekEndDate", WHPDate.date(TreatmentWeekInstance.currentWeekInstance().endDate()).value());
    }
}