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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class WeeklyAdherenceControllerTest {

    WeeklyAdherenceController weeklyAdherenceController;

    @Mock
    AllPatients allPatients;
    @Mock
    Model uiModel;

    @Before
    public void setUp() {
        initMocks(this);
        weeklyAdherenceController = new WeeklyAdherenceController(allPatients);
    }

    @Test
    public void shouldReturnPatientWithId() {
        Patient patient = PatientBuilder.startRecording().withDefaults().build();
        when(allPatients.findByPatientId("patientId")).thenReturn(patient);

        weeklyAdherenceController.update("patientId", uiModel);
        verify(uiModel).addAttribute(eq("patientId"),eq(patient.getPatientId()));
        ArgumentCaptor<WeeklyAdherenceLog> adherenceForm = ArgumentCaptor.forClass(WeeklyAdherenceLog.class);
        verify(uiModel).addAttribute(eq("weeklyAdherenceLog"), adherenceForm.capture());
        assertEquals(3, adherenceForm.getValue().getAllDailyAdherenceLogs().size());
    }

}
