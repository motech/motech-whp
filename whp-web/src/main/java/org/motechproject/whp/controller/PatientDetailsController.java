package org.motechproject.whp.controller;

import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.util.DateUtil.today;

@Controller
@RequestMapping(value = "/patients")
public class PatientDetailsController extends BaseWebController{


    private PatientService patientService;
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    private ProviderService providerService;
    private ProviderRemarksService providerRemarksService;

    @Autowired
    public PatientDetailsController(PatientService patientService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, ProviderService providerService, ProviderRemarksService providerRemarksService) {
        this.patientService = patientService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.providerService = providerService;
        this.providerRemarksService = providerRemarksService;
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = patientService.findByPatientId(patientId);
        treatmentUpdateOrchestrator.updateDoseInterruptions(patient);
        setupDashboardModel(uiModel, request, patient);
        return "patient/show";
    }

    private void setupDashboardModel(Model uiModel, HttpServletRequest request, Patient patient) {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        Treatment currentTreatment = patient.getCurrentTherapy().getCurrentTreatment();
        Provider provider = providerService.findByProviderId(currentTreatment.getProviderId());

        uiModel.addAttribute("patient", new PatientInfo(patient, provider));
        uiModel.addAttribute("phaseStartDates", phaseStartDates);
        uiModel.addAttribute("today", WHPDate.date(today()).value());
        uiModel.addAttribute("cmfAdminRemarks", patientService.getCmfAdminRemarks(patient));
        uiModel.addAttribute("providerRemarks", providerRemarksService.getRemarks(patient));

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotEmpty(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
    }

}
