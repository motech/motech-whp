package org.motechproject.whp.controller;

import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;
import org.motechproject.flash.Flash;
import org.motechproject.model.DayOfWeek;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.domain.*;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
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

import static org.motechproject.whp.adherence.domain.PillStatus.NotTaken;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;
import static org.motechproject.whp.criteria.UpdateAdherenceCriteria.canUpdate;
import static org.motechproject.whp.refdata.domain.WHPConstants.UNKNOWN;
import static org.motechproject.whp.uimodel.PillDays.takenDays;

@Controller
@RequestMapping(value = "/adherence")
@ExcelDataSource(name = "adherence")
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
        prepareModel(patient, uiModel, adherence);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId,
                         String categoryCode,
                         String remarks,
                         WeeklyAdherenceForm weeklyAdherenceForm,
                         HttpServletRequest httpServletRequest) {

        MotechUser authenticatedUser = loggedInUser(httpServletRequest);
        TreatmentCategory category = allTreatmentCategories.findByCode(categoryCode);

        AuditParams auditParams = new AuditParams(authenticatedUser.getUserName(), AdherenceSource.WEB, remarks);
        adherenceService.recordAdherence(patientId, weeklyAdherence(weeklyAdherenceForm, category), auditParams);
        Flash.out("message", "Adherence Saved For Patient : " + patientId, httpServletRequest);
        return "redirect:/";
    }

    public WeeklyAdherence weeklyAdherence(WeeklyAdherenceForm weeklyAdherenceForm, TreatmentCategory treatmentCategory) {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(new TreatmentWeek(weeklyAdherenceForm.getReferenceDate()));
        Patient patient = allPatients.findByPatientId(weeklyAdherenceForm.getPatientId());

        List<DayOfWeek> takenDays = takenDays(treatmentCategory, weeklyAdherenceForm.getNumberOfDosesTaken());
        for (DayOfWeek pillDay : treatmentCategory.getPillDays()) {
            PillStatus pillStatus = (takenDays.contains(pillDay)) ? Taken : NotTaken;
            Treatment treatment = patient.getTreatment(weeklyAdherence.getWeek().dateOf(pillDay));
            // AAAAAAAAhhhh redundant code : need to fix
            if (treatment == null) {
                weeklyAdherence.addAdherenceLog(pillDay, patient.getPatientId(), pillStatus, UNKNOWN, UNKNOWN, UNKNOWN);
            } else {
                weeklyAdherence.addAdherenceLog(pillDay, patient.getPatientId(), pillStatus, treatment.getTherapyDocId(), treatment.getProviderId(), treatment.getTbId());
            }
        }
        return weeklyAdherence;
    }

    @DataProvider
    public List<Adherence> adherenceReport(int pageNumber) {
        return adherenceService.allAdherenceData(pageNumber - 1, 10000);
    }

    private void prepareModel(Patient patient, Model uiModel, WeeklyAdherence adherence) {
        TreatmentCategory category = allTreatmentCategories.findByCode(patient.currentTherapy().getTreatmentCategory().getCode());
        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(adherence, patient);

        uiModel.addAttribute("referenceDate", weeklyAdherenceForm.getReferenceDateString());
        uiModel.addAttribute("categoryCode", category.getCode());
        uiModel.addAttribute("adherence", weeklyAdherenceForm);
        uiModel.addAttribute("totalDoses", category.getDosesPerWeek());
        uiModel.addAttribute("readOnly", !(canUpdate(patient)));
    }
}