package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.repository.AllDistricts;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.WHPDate.date;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;

    @Mock
    PatientService patientService;
    @Mock
    ProviderService providerService;
    @Mock
    PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    @Mock
    WHPAdherenceService whpAdherenceService;
    @Mock
    AllDistricts allDistrictsCache;

    AbstractMessageSource messageSource;

    Patient patient;
    Provider provider;
    PatientController patientController;

    List<District> districts = asList(new District("Vaishali"), new District("Begusarai"));

    private static final String LOGGED_IN_USER_NAME = "username";


    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        setupMessageSource();
        setUpLoggedInUser();
        patientController = new PatientController(patientService, whpAdherenceService, phaseUpdateOrchestrator, providerService, messageSource, allDistrictsCache);
        patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.fetchByProviderId(providerId)).thenReturn(provider);
        when(allDistrictsCache.getAll()).thenReturn(districts);
    }

    private void setupMessageSource() {
        messageSource = new StaticMessageSource();
        ((StaticMessageSource) messageSource).addMessage("dates.changed.message", Locale.ENGLISH, "message");
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        PatientInfo patientInfo = new PatientInfo(patient);
        when(patientService.getAllWithActiveTreatmentForProvider("providerId")).thenReturn(patientsForProvider);

        String view = patientController.listByProvider("providerId", uiModel, request);

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
                .andExpect(model().size(3))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
                .andExpect(forwardedUrl("patient/show"));
    }

    @Test
    public void shouldReturnDashBoardPrintView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
        standaloneSetup(patientController).build()
                .perform(get("/patients/print/" + patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
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

        verify(phaseUpdateOrchestrator, times(1)).updateDoseInterruptions(patient);
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
        verify(phaseUpdateOrchestrator).adjustPhaseStartDates(patient.getPatientId(), date(phaseStartDates.getIpStartDate()).date(), null, null);
        assertEquals("redirect:/patients/show?patientId=" + patient.getPatientId(), view);
    }

    @Test
    public void shouldAddRemarkToPatientsTherapyAndShowPatient() {
        String remark = "remark";
        String view = patientController.addRemark(patient.getPatientId(), remark, request);
        verify(patientService).addRemark(patient.getPatientId(), remark, LOGGED_IN_USER_NAME);
        assertEquals("redirect:/patients/show?patientId=" + patient.getPatientId(), view);
    }

    @Test
    public void shouldListAllPatientsBelongingToTheDefaultDistrict() {
        List<Patient> patients = asList(new Patient());
        String firstDistrict = districts.get(0).getName();
        when(patientService.searchBy(firstDistrict)).thenReturn(patients);

        assertEquals("patient/list", patientController.list(uiModel));
        verify(uiModel).addAttribute(PatientController.PATIENT_LIST, patients);
        verify(patientService).searchBy(firstDistrict);
    }

    @Test
    public void shouldSearchForPatientsByDistrict() {
        Patient patientUnderProviderA = new PatientBuilder().withDefaults().withTreatmentUnderProviderId("provider1").withTreatmentUnderDistrict("Vaishali").build();
        Patient patientUnderProviderB = new PatientBuilder().withDefaults().withTreatmentUnderProviderId("provider2").withTreatmentUnderDistrict("Vaishali").build();

        when(patientService.searchBy("Vaishali")).thenReturn(asList(patientUnderProviderA, patientUnderProviderB));

        patientController.filterByDistrictAndProvider("Vaishali", "", uiModel);

        ArgumentCaptor<List> patientsCaptor = forClass(List.class);
        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), patientsCaptor.capture());
        assertArrayEquals(new Patient[]{patientUnderProviderA, patientUnderProviderB}, patientsCaptor.getValue().toArray());
    }

    @Test
    public void shouldSearchForPatientsByDistrictAndProvider() {
        String providerId = "provider1";
        String district = "Vaishali";
        Patient patientUnderProviderA = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).withTreatmentUnderDistrict("some other district").build();

        Provider provider = new Provider(providerId, "", district, DateUtil.now());
        when(providerService.fetchByProviderId(providerId)).thenReturn(provider);
        when(patientService.getAllWithActiveTreatmentForProvider(providerId)).thenReturn(asList(patientUnderProviderA));

        patientController.filterByDistrictAndProvider(district, providerId, uiModel);

        ArgumentCaptor<List> patientsCaptor = forClass(List.class);
        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), patientsCaptor.capture());
        assertArrayEquals(new Patient[]{patientUnderProviderA}, patientsCaptor.getValue().toArray());
    }

    @Test
    public void shouldPopulateAllDistrictInModelForListPage() {
        patientController.list(uiModel);

        verify(uiModel).addAttribute(PatientController.DISTRICT_LIST, districts);
        verify(uiModel).addAttribute(PatientController.SELECTED_DISTRICT, districts.get(0).getName());
    }

    private void setUpLoggedInUser() {
        HttpSession session = mock(HttpSession.class);
        MotechUser loggedInUser = mock(MotechUser.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(loggedInUser);
        when(loggedInUser.getUserName()).thenReturn(LOGGED_IN_USER_NAME);
    }
}
