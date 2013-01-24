package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.service.PatientPagingService;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientControllerTest extends BaseControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;

    @Mock
    PatientService patientService;
    @Mock
    ProviderService providerService;
    @Mock
    PatientPagingService patientPagingService;

    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    WHPAdherenceService whpAdherenceService;
    @Mock
    AllDistricts allDistrictsCache;
    @Mock
    TreatmentCardService treatmentCardService;
    @Mock
    HttpSession session;
    @Mock
    ProviderRemarksService providerRemarksService;

    AbstractMessageSource messageSource;

    Patient patient;
    Provider provider;
    PatientController patientController;

    List<District> districts = asList(new District("Vaishali"), new District("Begusarai"));

    private static final String LOGGED_IN_USER_NAME = "username";
    private List<TherapyRemark> cmfAdminRemarks;
    private List<AuditLog> auditLogs;


    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        setupMessageSource();
        when(request.getSession()).thenReturn(session);
        setupLoggedInUser(session, LOGGED_IN_USER_NAME);

        patientController = new PatientController(patientService, patientPagingService, whpAdherenceService, treatmentCardService, treatmentUpdateOrchestrator, providerService, messageSource, allDistrictsCache, providerRemarksService);
        patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        when(allDistrictsCache.getAll()).thenReturn(districts);
        cmfAdminRemarks = mock(List.class);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
        auditLogs = mock(List.class);
        when(providerRemarksService.getRemarks(patient)).thenReturn(auditLogs);
    }

    private void setupMessageSource() {
        messageSource = new StaticMessageSource();
        ((StaticMessageSource) messageSource).addMessage("dates.changed.message", Locale.ENGLISH, "message");
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        PatientInfo patientInfo = new PatientInfo(patient);
        when(patientService.getAllWithActiveTreatmentForProvider(LOGGED_IN_USER_NAME)).thenReturn(patientsForProvider);
        String view = patientController.listByProvider(uiModel, request);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), captor.capture());

        assertEquals(patientInfo, captor.getValue().get(0));
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldReturnDashBoardView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
        standaloneSetup(patientController).build()
                .perform(get("/patients/show").param("patientId", patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("cmfAdminRemarks", cmfAdminRemarks))
                .andExpect(model().attribute("providerRemarks", auditLogs))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
                .andExpect(forwardedUrl("patient/show"));
    }

    @Test
    public void shouldReturnDashBoardPrintView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
        TreatmentCard treatmentCard = new TreatmentCard(patient);
        when(treatmentCardService.treatmentCard(patient)).thenReturn(treatmentCard);
        standaloneSetup(patientController).build()
                .perform(get("/patients/print/" + patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("treatmentCard", treatmentCard))
                .andExpect(forwardedUrl("patient/print"));
    }

    @Test
    public void shouldDisplayAdherenceUpdatedMessageIfAdherenceWasUpdated() {
        when(request.getAttribute("flash.in.message")).thenReturn("message");
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel).addAttribute("message", "message");
    }

    @Test
    public void shouldUpdateDoseInterruptionsOnPatientWhenNavigatedToDashboardPage() {
        patientController.show(patient.getPatientId(), uiModel, request);

        verify(treatmentUpdateOrchestrator, times(1)).updateDoseInterruptions(patient);
    }

    @Test
    public void shouldNotDisplayAdherenceUpdatedMessageIfAdherenceWasNotUpdated() {
        when(request.getAttribute(anyString())).thenReturn(null);
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel, never()).addAttribute(eq("message"), any());
    }

    @Test
    public void shouldUpdatePatientPhaseStartDatesAndShowPatient() {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("21/05/2012");

        String view = patientController.adjustPhaseStartDates(patient.getPatientId(), phaseStartDates, request);
        verify(treatmentUpdateOrchestrator).adjustPhaseStartDates(patient.getPatientId(), WHPDate.date(phaseStartDates.getIpStartDate()).date(), null, null);
        assertEquals("redirect:/patients/show?patientId=" + patient.getPatientId(), view);
    }

    @Test
    public void shouldAddRemarkToPatientsTherapyAndShowPatient() {
        String remark = "remark";
        List<TherapyRemark> cmfAdminRemarks = mock(List.class);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
        List<AuditLog> providerRemarks = mock(List.class);
        when(providerRemarksService.getRemarks(patient)).thenReturn(providerRemarks);

        String view = patientController.addRemark(uiModel,patient.getPatientId(), remark, request);

        verify(patientService).addRemark(patient, remark, LOGGED_IN_USER_NAME);
        verify(uiModel).addAttribute("cmfAdminRemarks", cmfAdminRemarks);
        verify(uiModel).addAttribute("providerRemarks", providerRemarks);
        assertEquals("patient/remarks", view);
    }

    @Test
    public void shouldSetUpUiModelForListAllPatients() throws Exception {
        standaloneSetup(patientController).build()
                .perform(get("/patients/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("districts", districts))
                .andExpect(view().name("patient/list"));
    }

    //TODO: Nishi, Abhi- future feature
    @Ignore
    @Test
    public void shouldFetchListOfPatientsIfDistrictNameIsPresentInCookies() throws Exception {
        String district = "Vaishali";
        Patient patientUnderDistrict = new PatientBuilder().withDefaults().withTreatmentUnderProviderId("providerid").withTreatmentUnderDistrict("some other district").build();

        List<Patient> expectedListOfPatients = asList(patientUnderDistrict);

        when(patientService.searchBy(district)).thenReturn(expectedListOfPatients);

        standaloneSetup(patientController).build()
                .perform(get("/patients/list").cookie(new Cookie(PatientController.SELECTED_DISTRICT, district)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedDistrict", district))
                .andExpect(model().attribute("patientList", expectedListOfPatients))
                .andExpect(model().attribute("districts", districts))
                .andExpect(view().name("patient/list"));
    }

    //TODO: Nishi, Abhi- future feature
    @Ignore
    @Test
    public void shouldFetchListOfPatientsIfProviderIdIsPresentInCookies() throws Exception {
        String providerId = "providerid";
        Patient patientUnderDistrict = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).withTreatmentUnderDistrict("some other district").build();

        List<Patient> expectedListOfPatients = asList(patientUnderDistrict);

        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(expectedListOfPatients);

        standaloneSetup(patientController).build()
                .perform(get("/patients/list")
                        .cookie(new Cookie(PatientController.SELECTED_PROVIDER, providerId),
                                new Cookie(PatientController.SELECTED_DISTRICT, "district")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedProvider", providerId))
                .andExpect(model().attribute("patientList", expectedListOfPatients))
                .andExpect(model().attribute("districts", districts))
                .andExpect(view().name("patient/list"));
    }

    @Test
    public void shouldSearchForPatientsByDistrict() throws Exception {
        String district = "Vaishali";
        Patient patientUnderDistrict = new PatientBuilder().withDefaults().withTreatmentUnderProviderId("providerid").withTreatmentUnderDistrict("some other district").build();

        List<Patient> expectedListOfPatients = asList(patientUnderDistrict);

        when(patientService.searchBy(district)).thenReturn(expectedListOfPatients);


        standaloneSetup(patientController).build()
                .perform(post("/patients/search").param("selectedDistrict", district))
                .andExpect(status().isOk())
                .andExpect(model().attribute("districts", districts))
                .andExpect(model().attribute("selectedDistrict", district))
                .andExpect(model().attribute("patientList", expectedListOfPatients))
                .andExpect(cookie().value("selectedDistrict", district))
                .andExpect(view().name("patient/patientList"));

        verify(patientService).searchBy(district);
    }

    @Test
    public void shouldSearchForPatientsByProvider() throws Exception {
        String providerId = "provider1";
        String district = "Vaishali";

        Provider provider = new Provider(providerId, "", district, DateUtil.now());
        Patient patientUnderProviderA = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).withTreatmentUnderDistrict("some other district").build();

        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        List<Patient> expectedListOfPatients = asList(patientUnderProviderA);
        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(expectedListOfPatients);

        standaloneSetup(patientController).build()
                .perform(post("/patients/search").param("selectedDistrict", district).param("selectedProvider", providerId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("districts", districts))
                .andExpect(model().attribute("selectedDistrict", district))
                .andExpect(model().attribute("selectedProvider", providerId))
                .andExpect(model().attribute("patientList", expectedListOfPatients))
                .andExpect(cookie().value("selectedDistrict", district))
                .andExpect(cookie().value("selectedProvider", providerId))
                .andExpect(view().name("patient/patientList"));

        verify(patientService).getAllWithActiveTreatmentForProvider(providerId);
    }

    @Test
    public void shouldFetchAllPatients() throws Exception {

        List<Patient> expectedLisOfPatients = asList(patient);

        when(patientPagingService.getAll()).thenReturn(expectedLisOfPatients);

        standaloneSetup(patientController).build()
                .perform(get("/patients/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("patientList", expectedLisOfPatients))
                .andExpect(view().name("patient/list"));

        verify(patientPagingService).getAll();

    }

}
