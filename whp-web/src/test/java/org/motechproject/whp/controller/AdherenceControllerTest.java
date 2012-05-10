package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.criteria.UpdateAdherenceCriteria;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.uimodel.AdherenceForm;
import org.springframework.ui.Model;

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
    Patient patient;

    @Mock
    WHPAdherenceService adherenceService;

    @Mock
    Model uiModel;

    ArgumentCaptors captors;

    AdherenceController adherenceController;

    @Before
    public void setUp() {
        setUpMocks();
        setUpPatient();
        adherenceController = new AdherenceController(allPatients, adherenceService, new UpdateAdherenceCriteria(allPatients));
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
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);

        String form = adherenceController.update(PATIENT_ID, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToAdherenceCard() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);

        adherenceController.update(PATIENT_ID, uiModel);
        verify(uiModel).addAttribute(eq(PATIENT_ID), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToAdherenceCard() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);
        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(adherence.getAdherenceLogs(), captors.adherenceForm.getValue().getAdherenceLogs());
    }

    @Test
    public void shouldPassReferenceDateToAdherenceCard() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);
        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("referenceDate"), eq("03-05-2012"));
    }

    @Test
    public void shouldCaptureAdherence() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        ArrayList<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>(adherence.getAdherenceLogs());
        adherenceController.update(PATIENT_ID, new AdherenceForm(adherence));

        ArgumentCaptor<WeeklyAdherence> captor = forClass(WeeklyAdherence.class);
        verify(adherenceService).recordAdherence(eq(PATIENT_ID), captor.capture());
        assertEquals(adherenceLogs, captor.getValue().getAdherenceLogs());
    }

    @Test
    public void shouldShowAnUpdateViewFromSundayTillTuesday() {
        WeeklyAdherence adherence = new WeeklyAdherence();
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);

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
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);

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
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);

        String form = adherenceController.update(PATIENT_ID, new AdherenceForm(adherence));
        assertEquals("forward:/", form);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<AdherenceForm> adherenceForm = forClass(AdherenceForm.class);
    }
}
