package org.motechproject.whp.controller;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
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
    AllPatients allPatients;
    @Mock
    AllAdherenceLogs allAdherenceLogs;
    @Mock
    ProviderService providerService;
    @Mock
    PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    PatientController patientController;

    Patient patient;
    Provider provider;

    @Before
    public void setup() {
        initMocks(this);
        patientController = new PatientController(allPatients, allAdherenceLogs, providerService, phaseUpdateOrchestrator);
        String providerId = "providerid";
        patient = new PatientBuilder().withDefaults().withProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.fetchByProviderId(providerId)).thenReturn(provider);
    }

    @Test
    public void shouldListPatientsForProvider() {
        String view = patientController.listByProvider("providerId", uiModel, request);
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        when(allPatients.getAllWithActiveTreatmentFor("providerId")).thenReturn(patientsForProvider);

        patientController.listByProvider("providerId", uiModel, request);
        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), same(patientsForProvider));
    }

    @Test
    public void shouldReturnDashBoardView() throws Exception {
        standaloneSetup(patientController).build()
                .perform(get("/patients/show").param("patientId", patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("provider", provider))
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
        when(request.getAttribute("flash.in.dateUpdatedMessage0")).thenReturn("message1");
        when(request.getAttribute("flash.in.dateUpdatedMessage1")).thenReturn("message2");
        when(request.getAttribute("flash.in.dateUpdatedMessage2")).thenReturn("");

        patientController.show(patient.getPatientId(), uiModel, request);

        ArgumentCaptor<List> messageList = ArgumentCaptor.forClass(List.class);

        verify(uiModel).addAttribute(eq("messages"), messageList.capture());

        assertArrayEquals(new String[]{"message1", "message2"}, messageList.getValue().toArray());
    }

    @Test
    public void shouldUpdatePatientPhaseStartDatesAndShowPatient() {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("21/05/2012");

        String view = patientController.adjustPhaseStartDates(patient.getPatientId(), phaseStartDates, request);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(allPatients).update(patientArgumentCaptor.capture());

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
    public void shouldPassAllPatientsAsModelToListAllView() {
        List<Patient> patients = emptyList();
        when(allPatients.getAllWithActiveTreatment()).thenReturn(patients);

        patientController.list(uiModel);
        verify(uiModel).addAttribute(PatientController.PATIENT_LIST, patients);
    }


}
