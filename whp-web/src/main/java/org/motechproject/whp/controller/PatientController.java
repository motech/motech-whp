package org.motechproject.whp.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.model.DayOfWeek;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.util.WHPDate.date;

@Controller
@RequestMapping(value = "/patients")
public class PatientController extends BaseWebController {

    public static final String DISTRICT_LIST = "districts";
    public static final String SELECTED_DISTRICT = "selectedDistrict";
    public static final String SELECTED_PROVIDER = "selectedProvider";

    public static final String PATIENT_LIST = "patientList";
    private ProviderService providerService;
    private PatientService patientService;

    private WHPAdherenceService whpAdherenceService;
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    private AbstractMessageSource messageSource;
    private AllDistricts allDistrictsCache;
    private TreatmentCardService treatmentCardService;
    private ProviderRemarksService providerRemarksService;


    @Autowired
    public PatientController(PatientService patientService,
                             WHPAdherenceService whpAdherenceService,
                             TreatmentCardService treatmentCardService,
                             TreatmentUpdateOrchestrator treatmentUpdateOrchestrator,
                             ProviderService providerService,
                             @Qualifier("messageBundleSource")
                             AbstractMessageSource messageSource,
                             AllDistricts allDistrictsCache,
                             ProviderRemarksService providerRemarksService) {

        this.patientService = patientService;
        this.whpAdherenceService = whpAdherenceService;
        this.treatmentCardService = treatmentCardService;
        this.allDistrictsCache = allDistrictsCache;
        this.providerService = providerService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.messageSource = messageSource;
        this.providerRemarksService = providerRemarksService;
    }

    @RequestMapping(value = "listByProvider", method = RequestMethod.GET)
    public String listByProvider(Model uiModel, HttpServletRequest request) {
        String idOfLoggedInProvider = loggedInUser(request).getUserName();
        List<Patient> patientsForProvider = patientService.getAllWithActiveTreatmentForProvider(idOfLoggedInProvider);
        prepareModelForListView(uiModel, patientsForProvider);
        passAdherenceSavedMessageToListView(uiModel, request);
        return "patient/listByProvider";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@CookieValue(value = SELECTED_DISTRICT, required = false) String districtName,
                       @CookieValue(value = SELECTED_PROVIDER, required = false) String providerId,
                       Model uiModel) {
        List<Patient> patients = getPatientsFor(districtName, providerId);
        prepareModelForListView(uiModel, patients, districtName, providerId);
        return "patient/list";
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("patientId") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = patientService.findByPatientId(patientId);
        treatmentUpdateOrchestrator.updateDoseInterruptions(patient);
        setupDashboardModel(uiModel, request, patient);
        return "patient/show";
    }

    @RequestMapping(value = "print/{id}", method = RequestMethod.GET)
    public String print(@PathVariable("id") String patientId, Model uiModel, HttpServletRequest request) {
        Patient patient = patientService.findByPatientId(patientId);
        setupPrintDashboardModel(uiModel, patient);
        return "patient/print";
    }

    public static String redirectToPatientDashboardURL(String patientId) {
        return "redirect:/patients/show?patientId=" + patientId;
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String filterByDistrictAndProvider(@RequestParam(value = "selectedDistrict", required = false) String districtName, @RequestParam(value = SELECTED_PROVIDER, required = false) String providerId, Model uiModel, HttpServletResponse response) {
        List<Patient> patients = getPatientsFor(districtName, providerId);
        prepareModelForListView(uiModel, patients, districtName, providerId);
        setSearchParamsIntoCookies(response, districtName, providerId);
        return "patient/patientList";
    }

    private void setSearchParamsIntoCookies(HttpServletResponse response, String districtName, String providerId) {
        setCookieValue(response, SELECTED_DISTRICT, districtName);
        setCookieValue(response, SELECTED_PROVIDER, providerId);
    }

    private List<Patient> getPatientsFor(String districtName, String providerId) {
        List<Patient> patients;
        if (isNotEmpty(providerId))
            patients = patientService.getAllWithActiveTreatmentForProvider(providerId);
        else if(isNotEmpty(districtName))
            patients = patientService.searchBy(districtName);
        else
            patients = new ArrayList<>();

        return patients;
    }

    @RequestMapping(value = "adjustPhaseStartDates", method = RequestMethod.POST)
    public String adjustPhaseStartDates(@RequestParam("patientId") String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        treatmentUpdateOrchestrator.adjustPhaseStartDates(
                patientId,
                date(phaseStartDates.getIpStartDate()).date(),
                date(phaseStartDates.getEipStartDate()).date(),
                date(phaseStartDates.getCpStartDate()).date()
        );
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
        String message = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (StringUtils.isNotEmpty(message)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, message);
        }
    }

    private void prepareModelForListView(Model uiModel, List<Patient> patientsForProvider) {
        List<PatientInfo> patientList = new ArrayList<>();
        for (Patient patient : patientsForProvider) {
            PatientInfo patientInfo = new PatientInfo(patient);
            patientList.add(patientInfo);
        }
        uiModel.addAttribute(PATIENT_LIST, patientList);
        uiModel.addAttribute("weekStartDate", WHPDate.date(TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate()).value());
        uiModel.addAttribute("weekEndDate", WHPDate.date(TreatmentWeekInstance.currentAdherenceCaptureWeek().endDate()).value());

    }

    private void prepareModelForListView(Model uiModel, List<Patient> patients, String districtName, String providerId) {
        uiModel.addAttribute(PATIENT_LIST, patients);
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute(SELECTED_DISTRICT, districtName);
        uiModel.addAttribute(SELECTED_PROVIDER, providerId);
        uiModel.addAttribute("lastSunday", WHPDate.date(TreatmentWeekInstance.currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday)).lucidValue());
    }



    private void flashOutDateUpdatedMessage(String patientId, PhaseStartDates phaseStartDates, HttpServletRequest httpServletRequest) {
        String ipStartDate = dateMessage(phaseStartDates.getIpStartDate());
        String eipStartDate = dateMessage(phaseStartDates.getEipStartDate());
        String cpStartDate = dateMessage(phaseStartDates.getCpStartDate());
        out(WHPConstants.NOTIFICATION_MESSAGE, messageSource.getMessage("dates.changed.message", new Object[]{patientId, ipStartDate, eipStartDate, cpStartDate}, Locale.ENGLISH), httpServletRequest);
    }

    private void setupDashboardModel(Model uiModel, HttpServletRequest request, Patient patient) {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        Treatment currentTreatment = patient.getCurrentTherapy().getCurrentTreatment();
        Provider provider = providerService.findByProviderId(currentTreatment.getProviderId());

        uiModel.addAttribute("patient", new PatientInfo(patient, provider));
        uiModel.addAttribute("phaseStartDates", phaseStartDates);
        uiModel.addAttribute("today", WHPDate.date(today()).value());
        setUpModelForRemarks(uiModel, patient);

        String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
        if (isNotEmpty(messages)) {
            uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
        }
    }

    private void setUpModelForRemarks(Model uiModel, Patient patient) {
        uiModel.addAttribute("cmfAdminRemarks", patientService.getCmfAdminRemarks(patient));
        uiModel.addAttribute("providerRemarks", providerRemarksService.getRemarks(patient));
    }

    private void setupPrintDashboardModel(Model uiModel, Patient patient) {
        Treatment currentTreatment = patient.getCurrentTherapy().getCurrentTreatment();
        Provider provider = providerService.findByProviderId(currentTreatment.getProviderId());

        uiModel.addAttribute("patient", new PatientInfo(patient, provider));
        uiModel.addAttribute("treatmentCard", treatmentCardService.treatmentCard(patient));
    }

    private String dateMessage(String date) {
        return isBlank(date) ? "Not Set" : date;
    }

    @RequestMapping(value = "transitionPhase/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") String patientId, @RequestParam("to") String phaseName) {
        treatmentUpdateOrchestrator.setNextPhase(patientId, Phase.valueOf(phaseName));
        return String.format("redirect:/patients/show?patientId=%s", patientId);
    }

    @RequestMapping(value = "addRemark/{id}", method = RequestMethod.POST)
    public String addRemark(Model uiModel, @PathVariable("id") String patientId, @RequestParam("patientRemark") String remark, HttpServletRequest httpServletRequest) {
        MotechUser authenticatedUser = loggedInUser(httpServletRequest);
        Patient patient = patientService.findByPatientId(patientId);
        patientService.addRemark(patient, remark, authenticatedUser.getUserName());
        setUpModelForRemarks(uiModel, patient);
        return "patient/remarks";
    }
}
