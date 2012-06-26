package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class AdherenceControllerTest extends BaseUnitTest {

    @Mock
    AllPatients allPatients;
    @Mock
    AllTreatmentCategories allTreatmentCategories;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;

    private Patient patient;

    private TreatmentCategory category;

    private ArgumentCaptors captors;

    private AdherenceController adherenceController;

    private final String remarks = "remarks";
    private final String providerUserName = "someProviderUserName";
    private final AuditParams auditParams = new AuditParams(providerUserName, AdherenceSource.WEB, remarks);

    @Before
    public void setUp() {
        setUpMocks();
        setUpPatient();
        adherenceController = new AdherenceController(allPatients, adherenceService, allTreatmentCategories);
        setupLoggedInUser(providerUserName);
    }

    private void setUpMocks() {
        initMocks(this);
        captors = new ArgumentCaptors();
    }

    private void setupTreatmentCategory() {
        category = new TreatmentCategory();
        category.setCode("01");
        category.setDosesPerWeek(3);
        category.setPillDays(asList(Monday, Wednesday, Friday));
        when(allTreatmentCategories.findByCode("01")).thenReturn(category);
    }

    private void setUpPatient() {
        patient = new PatientBuilder().withDefaults().withStatus(PatientStatus.Open).build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        setupTreatmentCategory();
    }

    @Test
    public void shouldShowAdherenceCard() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummary();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherenceSummary);

        String form = adherenceController.update(PATIENT_ID, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToAdherenceCard() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherenceSummary);

        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(PatientBuilder.PATIENT_ID, captors.adherenceForm.getValue().getPatientId());
    }

    @Test
    public void shouldCaptureAdherence() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        adherenceController.update(PATIENT_ID, remarks, new WeeklyAdherenceForm(adherenceSummary, patient), request);

        ArgumentCaptor<WeeklyAdherenceSummary> captor = forClass(WeeklyAdherenceSummary.class);
        verify(adherenceService).recordAdherence(captor.capture(), eq(auditParams));
        assertEquals(category.getPillDays().size(), captor.getValue().getDosesTaken());
    }

    @Test
    public void shouldShowAnUpdateViewFromSundayTillTuesday() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummary();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherenceSummary);

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
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummary();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherenceSummary);

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
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        when(adherenceService.currentWeekAdherence(patient)).thenReturn(adherenceSummary);

        String form = adherenceController.update(PATIENT_ID, remarks, new WeeklyAdherenceForm(adherenceSummary, patient), request);
        assertEquals("redirect:/", form);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<WeeklyAdherenceForm> adherenceForm = forClass(WeeklyAdherenceForm.class);
    }

    private void setupLoggedInUser(String userName) {
        HttpSession session = mock(HttpSession.class);
        MotechUser loggedInUser = mock(MotechUser.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(loggedInUser);
        when(loggedInUser.getUserName()).thenReturn(userName);
    }
}