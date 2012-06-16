package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.contract.DailyAdherenceRequest;
import org.motechproject.whp.contract.TreatmentCardModel;
import org.motechproject.whp.contract.UpdateAdherenceRequest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.service.TreatmentCardService;
import org.motechproject.whp.uimodel.PatientDTO;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    TreatmentCardService treatmentCardService;

    PatientController patientController;

    PatientBuilder patientBuilder;

    Patient patient;

    @Before
    public void setup() {
        initMocks(this);
        patientController = new PatientController(allPatients, allAdherenceLogs, treatmentCardService);
        patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
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
    public void shouldLoadPatientDashboard() {
        patientController.show(patient.getPatientId(), uiModel, request);

        ArgumentCaptor<PatientDTO> patientDTOArgumentCaptor = ArgumentCaptor.forClass(PatientDTO.class);

        verify(uiModel).addAttribute(eq("patient"), patientDTOArgumentCaptor.capture());
        verify(uiModel).addAttribute(eq("patientId"), same(patient.getPatientId()));

        assertEquals(new PatientDTO(patient), patientDTOArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnDashBoardView() {
        String view = patientController.show(patient.getPatientId(), uiModel, request);

        assertEquals("patient/show", view);
    }

    @Test
    public void shouldReturnTreatmentCardModelToView() {
        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        when(treatmentCardService.getIntensivePhaseTreatmentCardModel(patient)).thenReturn(treatmentCardModel);

        patientController.show(patient.getPatientId(), uiModel, request);

        verify(uiModel).addAttribute("treatmentCard", treatmentCardModel);
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
    public void shouldUpdatePatientAndRedirectToDashboardAfterSettingDates() {
        PatientDTO patientDTO = new PatientDTO(patient);
        patientDTO.setIpStartDate("21/05/2012");

        String view = patientController.update(patient.getPatientId(), patientDTO, request);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(allPatients).update(patientArgumentCaptor.capture());

        assertEquals(new LocalDate(2012, 5, 21), patientArgumentCaptor.getValue().latestTherapy().getStartDate());
        assertEquals("redirect:/patients/dashboard?patientId=" + patient.getPatientId(), view);
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

    @Test
    public void shouldSaveAdherenceData() throws IOException {
        String delta = "{patientId:test , dailyAdherenceRequests:[{day:6,month:7,year:2012,pillStatus:1},{day:13,month:8,year:2012,pillStatus:2}]}";
        UpdateAdherenceRequest adherenceData = new UpdateAdherenceRequest();
        adherenceData.setPatientId("test");
        DailyAdherenceRequest dailyAdherenceRequest1 = new DailyAdherenceRequest(6, 7, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(13, 8, 2012, 2);
        adherenceData.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2));

        setUpUserInSession("username");
        Patient patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(adherenceData.getPatientId())).thenReturn(patient);
        String view = patientController.saveTreatmentCard(delta, uiModel, request);
        assertEquals("patient/show", view);

        verify(treatmentCardService, times(1)).addLogsForPatient(adherenceData, "username", patient);
    }

    private void setUpUserInSession(String username) {
        HttpSession httpSession = mock(HttpSession.class);
        MotechUser user = mock(MotechUser.class);
        when(user.getUserName()).thenReturn(username);
        when(httpSession.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(user);
        when(request.getSession()).thenReturn(httpSession);
    }
}
