package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.repository.AllDistricts;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    public static final String DISTRICT_LIST = "districts";
    public static final String SELECTED_DISTRICT = "selectedDistrict";
    public static final String SELECTED_PROVIDER_ID = "providerId";
    public static final String PATIENT_LIST = "patientList";

    private ProviderService providerService;
    private PatientService patientService;

    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    private AbstractMessageSource messageSource;
    private AllDistricts allDistrictsCache;

    @Autowired
    public PatientController(PatientService patientService,
                             PhaseUpdateOrchestrator phaseUpdateOrchestrator,
                             ProviderService providerService,
                             @Qualifier("messageBundleSource")
                             AbstractMessageSource messageSource,
                             AllDistricts allDistrictsCache) {

        this.patientService = patientService;
        this.allDistrictsCache = allDistrictsCache;
        this.providerService = providerService;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "listByProvider", method = RequestMethod.GET)
    public String listByProvider(@RequestParam("provider") String providerId, Model uiModel, HttpServletRequest request) {
        List<Patient> patientsForProvider = patientService.getAllWithActiveTreatmentForProvider(providerId);
        prepareModelForListView(uiModel, patientsForProvider);
        passAdherenceSavedMessageToListView(uiModel, request);
        return "patient/listByProvider";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model uiModel) {
        String districtName = allDistrictsCache.getAll().get(0).getName();
        String providerId = "";
        List<Patient> patients = patientService.searchBy(districtName, providerId);
        prepareModelForListView(uiModel, patients, districtName, providerId);
        return "patient/list";
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = patientService.findByPatientId(patientId);
        setupDashboardModel(uiModel, request, patient);
        return "patient/show";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String filterByDistrictAndProvider(@RequestParam("selectedDistrict") String districtName, @RequestParam("selectedProvider") String providerId, Model uiModel) {
        List<Patient> patients = patientService.searchBy(districtName, providerId);
        prepareModelForListView(uiModel, patients, districtName, providerId);
        return "patient/patientList";
    }

    @RequestMapping(value = "adjustPhaseStartDates", method = RequestMethod.POST)
    public String adjustPhaseStartDates(@RequestParam("patientId") String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        Patient updatedPatient = phaseStartDates.mapNewPhaseInfoToPatient(patientService.findByPatientId(patientId));
        patientService.update(updatedPatient);
        phaseUpdateOrchestrator.recomputePillCount(updatedPatient.getPatientId());
        flashOutDateUpdatedMessage(patientId, phaseStartDates, httpServletRequest);
        return String.format("redirect:/patients/show?patientId=%s", patientId);
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.POST)
    public String update(@RequestParam("patientId") String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        Patient updatedPatient = phaseStartDates.mapNewPhaseInfoToPatient(patientService.findByPatientId(patientId));
        patientService.update(updatedPatient);
        flashOutDateUpdatedMessage(patientId, phaseStartDates, httpServletRequest);
        return String.format("redirect:/patients/dashboard?patientId=%s", patientId);
    }

    private void passAdherenceSavedMessageToListView(Model uiModel, HttpServletRequest request) {
        String message = in("message", request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute("message", message);
        }
    }

    private void prepareModelForListView(Model uiModel, List<Patient> patientsForProvider) {
        uiModel.addAttribute(PATIENT_LIST, patientsForProvider);
    }

    private void prepareModelForListView(Model uiModel, List<Patient> patients, String districtName, String providerId) {
        uiModel.addAttribute(PATIENT_LIST, patients);
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute(SELECTED_DISTRICT, districtName);
        uiModel.addAttribute(SELECTED_PROVIDER_ID, providerId);
    }

    private void flashOutDateUpdatedMessage(String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        String ipStartDate = dateMessage(phaseStartDates.getIpStartDate());
        String eipStartDate = dateMessage(phaseStartDates.getEipStartDate());
        String cpStartDate = dateMessage(phaseStartDates.getCpStartDate());
        out("messages", messageSource.getMessage("dates.changed.message", new Object[]{patientId, ipStartDate, eipStartDate, cpStartDate}, Locale.ENGLISH), httpServletRequest);
    }

    private void setupDashboardModel(Model uiModel, HttpServletRequest request, Patient patient) {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        Provider provider = providerService.fetchByProviderId(patient.providerId());

        uiModel.addAttribute("patient", new PatientInfo(patient,provider));
        uiModel.addAttribute("phaseStartDates", phaseStartDates);

        String messages = in("messages", request);
        if (isNotEmpty(messages)) {
            uiModel.addAttribute("messages", messages);
        }
    }

    private String dateMessage(String date) {
        return isBlank(date) ? "Not Set" : date;
    }

    @RequestMapping(value = "transitionPhase/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") String patientId, @RequestParam("to") String phaseName) {
        phaseUpdateOrchestrator.setNextPhase(patientId, PhaseName.valueOf(phaseName));
        return String.format("redirect:/patients/show?patientId=%s", patientId);
    }
}
