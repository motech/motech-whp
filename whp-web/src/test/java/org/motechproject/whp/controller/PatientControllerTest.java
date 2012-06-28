package org.motechproject.whp.controller;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.objectcache.AllDistrictsCache;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
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
    AllDistrictsCache allDistrictsCache;
    List<District> districts = asList(new District("Vaishali"), new District("Begusarai"));

    AbstractMessageSource messageSource;
    Patient patient;
    Provider provider;

    PatientController patientController;

    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        setupMessageSource();
        patientController = new PatientController(patientService, phaseUpdateOrchestrator, providerService, messageSource, allDistrictsCache);
        patient = new PatientBuilder().withDefaults().withProviderId(providerId).build();
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
    public void shouldListPatientsForProvider() {
        String view = patientController.listByProvider("providerId", uiModel, request);
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        when(patientService.getAllWithActiveTreatmentForProvider("providerId")).thenReturn(patientsForProvider);

        patientController.listByProvider("providerId", uiModel, request);
        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), same(patientsForProvider));
    }

    @Test
    public void shouldReturnDashBoardView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
        standaloneSetup(patientController).build()
                .perform(get("/patients/show").param("patientId", patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
                .andExpect(forwardedUrl("patient/show"));
    }

    @Test
    public void shouldNotAddMessagesIfTheyAreNotPresentInFlashScope() {
        when(request.getAttribute(anyString())).thenReturn(null);
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel, never()).addAttribute(eq("messages"), any());
    }

    @Test
    public void shouldAddMessagesIfPresentInFlashScope() {
        when(request.getAttribute("flash.in.messages")).thenReturn("message");
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel).addAttribute("messages", "message");
    }

    @Test
    public void shouldUpdatePatientPhaseStartDatesAndShowPatient() {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("21/05/2012");

        String view = patientController.adjustPhaseStartDates(patient.getPatientId(), phaseStartDates, request);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(patientService).update(patientArgumentCaptor.capture());

        assertEquals(new LocalDate(2012, 5, 21), patientArgumentCaptor.getValue().currentTherapy().getStartDate());
        assertEquals("redirect:/patients/show?patientId=" + patient.getPatientId(), view);
    }

    @Test
    public void shouldRecomputePillCountWhenPhaseDatesAreSet() {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        patientController.adjustPhaseStartDates(patient.getPatientId(), phaseStartDates, request);
        verify(phaseUpdateOrchestrator).recomputePillCount(patient.getPatientId());
    }

    @Test
    public void shouldShowListAllViewOnRequest() {
        assertEquals("patient/list", patientController.list(uiModel));
    }

    @Test
    public void shouldPassAllPatientsBelongingToFirstDistrictToListPage() {
        List<Patient> patients = asList(new Patient());
        String firstDistrict = districts.get(0).getName();
        when(patientService.getAllWithActiveTreatmentForDistrict(firstDistrict)).thenReturn(patients);

        patientController.list(uiModel);
        verify(uiModel).addAttribute(PatientController.PATIENT_LIST, patients);
        verify(patientService).getAllWithActiveTreatmentForDistrict(firstDistrict);
    }

    @Test
    public void shouldPassDistrictsToModelForListPage() {
        patientController.list(uiModel);

        verify(uiModel).addAttribute(PatientController.DISTRICT_LIST, districts);
        verify(uiModel).addAttribute(PatientController.SELECTED_DISTRICT, districts.get(0).getName());
    }
}
