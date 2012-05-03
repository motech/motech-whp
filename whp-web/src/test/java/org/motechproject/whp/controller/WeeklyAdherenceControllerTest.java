package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceLog;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.ui.Model;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.Monday;

public class WeeklyAdherenceControllerTest {

    @Mock
    AllPatients allPatients;
    Patient patient;

    @Mock
    Model uiModel;
    ArgumentCaptors captors;

    WeeklyAdherenceController weeklyAdherenceController;

    @Before
    public void setUp() {
        setUpMocks();
        setUpPatient();
        weeklyAdherenceController = new WeeklyAdherenceController(allPatients);
    }

    private void setUpMocks() {
        initMocks(this);
        captors = new ArgumentCaptors();
    }

    private void setUpPatient() {
        patient = PatientBuilder.startRecording().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldShowTreatmentForm() {
        String form = weeklyAdherenceController.update("patientId", uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToTreatmentForm() {
        weeklyAdherenceController.update("patientId", uiModel);
        verify(uiModel).addAttribute(eq("patientId"), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToTreatmentForm() {
        weeklyAdherenceController.update("patientId", uiModel);

        verify(uiModel).addAttribute(eq("weeklyAdherenceLog"), captors.adherenceForm.capture());
        assertEquals(3, captors.adherenceForm.getValue().getAllDailyAdherenceLogs().size());
    }

    @Test
    public void shouldShowTreatmentFormAfterCapturingAdherence() {
        WeeklyAdherenceLog weeklyAdherenceLog = new WeeklyAdherenceLog(asList(Monday));
        String form = weeklyAdherenceController.update("patientId", weeklyAdherenceLog, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToTreatmentFormAfterCapturingAdherence() {
        WeeklyAdherenceLog weeklyAdherenceLog = new WeeklyAdherenceLog(asList(Monday));
        weeklyAdherenceController.update("patientId", weeklyAdherenceLog, uiModel);
        verify(uiModel).addAttribute(eq("patientId"), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToTreatmentFormAfterCapturingAdherence() {
        WeeklyAdherenceLog weeklyAdherenceLog = new WeeklyAdherenceLog(asList(Monday));
        weeklyAdherenceController.update("patientId", weeklyAdherenceLog, uiModel);

        verify(uiModel).addAttribute(eq("weeklyAdherenceLog"), captors.adherenceForm.capture());
        assertEquals(3, captors.adherenceForm.getValue().getAllDailyAdherenceLogs().size());
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<WeeklyAdherenceLog> adherenceForm = ArgumentCaptor.forClass(WeeklyAdherenceLog.class);
    }
}
