package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.criteria.UpdateAdherenceCriteria;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.*;

public class AdherenceControllerTest extends BaseUnitTest {

    public static final String PATIENT_ID = "patientId";

    @Mock
    AllPatients allPatients;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;

    private String loggedInUserName;
    private Patient patient;
    private ArgumentCaptors captors;
    private AdherenceController adherenceController;

    @Before
    public void setUp() {
        setUpMocks();
        setUpPatient();
        adherenceController = new AdherenceController(allPatients, adherenceService);
        loggedInUserName = "someProviderUserName";
        setupLoggedInUser(loggedInUserName);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    private void setUpMocks() {
        initMocks(this);
        captors = new ArgumentCaptors();
    }

    private void setUpPatient() {
        patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withStatus(PatientStatus.Open).build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldShowAdherenceCard() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        String form = adherenceController.update(PATIENT_ID, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToAdherenceCard() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);
        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(adherence.getAdherenceLogs(), captors.adherenceForm.getValue().getAdherenceList());
    }

    @Test
    public void shouldPassReferenceDateToAdherenceCard() {
        mockCurrentDate(DateUtil.newDate(2012, 5, 10));
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);
        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("referenceDate"), eq("04-05-2012"));
    }

    @Test
    public void shouldCaptureAdherence() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        ArrayList<Adherence> adherences = new ArrayList<Adherence>(adherence.getAdherenceLogs());
        adherenceController.update(PATIENT_ID, new WeeklyAdherenceForm(adherence, new TreatmentInterruptions()), request);

        ArgumentCaptor<WeeklyAdherence> captor = forClass(WeeklyAdherence.class);
        verify(adherenceService).recordAdherence(eq(PATIENT_ID), captor.capture(), eq(loggedInUserName), eq(AdherenceSource.WEB));
        assertEquals(adherences, captor.getValue().getAdherenceLogs());
    }

    @Test
    public void shouldShowAnUpdateViewFromSundayTillTuesday() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        LocalDate today = DateUtil.today();
        for (LocalDate date :
                asList(
                        today.withDayOfWeek(Sunday.getValue()),
                        today.withDayOfWeek(Monday.getValue()),
                        today.withDayOfWeek(Tuesday.getValue())
                )) {
            mockCurrentDate(date);
            adherenceController.update(PATIENT_ID, uiModel);
            verify(uiModel).addAttribute(eq("readOnly"), eq(false));
            reset(uiModel);
        }
    }

    @Test
    public void shouldShowAReadOnlyViewFromWednesdayTillSaturday() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        LocalDate today = DateUtil.today();
        for (LocalDate date :
                asList(
                        today.withDayOfWeek(Wednesday.getValue()),
                        today.withDayOfWeek(Thursday.getValue()),
                        today.withDayOfWeek(Friday.getValue()),
                        today.withDayOfWeek(Saturday.getValue())
                )) {
            mockCurrentDate(date);
            adherenceController.update(PATIENT_ID, uiModel);
            verify(uiModel).addAttribute(eq("readOnly"), eq(true));
            reset(uiModel);
        }
    }

    @Test
    public void shouldShowForwardToProviderHomeAfterCapturingAdherence() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        String form = adherenceController.update(PATIENT_ID, new WeeklyAdherenceForm(adherence, new TreatmentInterruptions()), request);
        assertEquals("forward:/", form);
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<WeeklyAdherenceForm> adherenceForm = forClass(WeeklyAdherenceForm.class);
    }

    private void setupLoggedInUser(String userName) {
        HttpSession session = mock(HttpSession.class);
        AuthenticatedUser loggedInUser = mock(AuthenticatedUser.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(loggedInUser);
        when(loggedInUser.getUsername()).thenReturn(userName);
    }
}
