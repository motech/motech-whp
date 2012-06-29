package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.request.PhaseTransitionRequest;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;

@Controller
@RequestMapping(value = "/patients")
public class PatientController extends BaseController {

    public static final String PATIENT_LIST = "patientList";
    AllPatients allPatients;
    AllAdherenceLogs allAdherenceLogs;
    private ProviderService providerService;
    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    private AbstractMessageSource messageSource;

    @Autowired
    public PatientController(AllPatients allPatients,
                             AllAdherenceLogs allAdherenceLogs,
                             PhaseUpdateOrchestrator phaseUpdateOrchestrator,
                             ProviderService providerService,
                             @Qualifier("messageBundleSource")
                             AbstractMessageSource messageSource) {
        this.allPatients = allPatients;
        this.allAdherenceLogs = allAdherenceLogs;
        this.providerService = providerService;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listByProvider(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = allPatients.getAllWithActiveTreatmentFor(providerId);
        passPatientsAsModelToListView(uiModel, patientsForProvider);
        passAdherenceSavedMessageToListView(uiModel, request);
        return "patient/listByProvider";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model uiModel) {
        List<Patient> patients = allPatients.getAllWithActiveTreatment();
        passPatientsAsModelToListView(uiModel, patients);
        return "patient/list";
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = allPatients.findByPatientId(patientId);
        setupDashboardModel(uiModel, request, patient);
        return "patient/show";
    }

    @RequestMapping(value = "adjustPhaseStartDates", method = RequestMethod.POST)
    public String adjustPhaseStartDates(@RequestParam("patientId") String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        Patient updatedPatient = phaseStartDates.mapNewPhaseInfoToPatient(allPatients.findByPatientId(patientId));
        allPatients.update(updatedPatient);
        phaseUpdateOrchestrator.recomputePillCount(updatedPatient.getPatientId());
        flashOutDateUpdatedMessage(patientId, phaseStartDates, httpServletRequest);
        return String.format("redirect:/patients/show?patientId=%s", patientId);
    }

    private void passAdherenceSavedMessageToListView(Model uiModel, HttpServletRequest request) {
        String message = in("message", request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute("message", message);
        }
    }

    private void passPatientsAsModelToListView(Model uiModel, List<Patient> patientsForProvider) {
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);
    }

    private void setupDashboardModel(Model uiModel, HttpServletRequest request, Patient patient) {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        uiModel.addAttribute("patient", patient);
        uiModel.addAttribute("phaseStartDates", phaseStartDates);
        uiModel.addAttribute("provider", providerService.fetchByProviderId(patient.providerId()));
        String messages = in("messages", request);
        if (isNotEmpty(messages)) {
            uiModel.addAttribute("messages", messages);
        }
    }

    private void flashOutDateUpdatedMessage(String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        String ipStartDate = dateMessage(phaseStartDates.getIpStartDate());
        String eipStartDate = dateMessage(phaseStartDates.getEipStartDate());
        String cpStartDate = dateMessage(phaseStartDates.getCpStartDate());
        out("messages", messageSource.getMessage("dates.changed.message", new Object[]{patientId, ipStartDate, eipStartDate, cpStartDate}, Locale.ENGLISH), httpServletRequest);
    }

    private String dateMessage(String date) {
        return isBlank(date) ? "Not Set" : date;
    }

    @RequestMapping(value = "transitionPhase", method = RequestMethod.POST)
    public String update(@RequestBody PhaseTransitionRequest phaseTransitionRequest, Model uiModel, HttpServletRequest request) {
        phaseUpdateOrchestrator.setNextPhase(phaseTransitionRequest.getPatientId(), phaseTransitionRequest.getPhaseToTransitionTo());
        return "";
    }
}
