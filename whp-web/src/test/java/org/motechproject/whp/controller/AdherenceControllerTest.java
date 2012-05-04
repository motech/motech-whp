package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.ui.Model;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceControllerTest {

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
        adherenceController = new AdherenceController(allPatients, adherenceService);
    }

    private void setUpMocks() {
        initMocks(this);
        captors = new ArgumentCaptors();
    }

    private void setUpPatient() {
        patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldShowAdherenceCard() {
        String form = adherenceController.update(PATIENT_ID, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToAdherenceCard() {
        adherenceController.update(PATIENT_ID, uiModel);
        verify(uiModel).addAttribute(eq(PATIENT_ID), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToAdherenceCard() {
        Adherence adherence = new Adherence();
        when(adherenceService.currentWeekAdherence(PATIENT_ID)).thenReturn(adherence);
        adherenceController.update(PATIENT_ID, uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(adherence, captors.adherenceForm.getValue());
    }

    @Test
    public void shouldCaptureAdherence() {
        Adherence adherence = new Adherence();
        adherenceController.update(PATIENT_ID, adherence);
        verify(adherenceService).recordAdherence(PATIENT_ID, adherence);
    }

    @Test
    public void shouldShowForwardToProviderHomeAfterCapturingAdherence() {
        Adherence adherence = new Adherence();
        String form = adherenceController.update(PATIENT_ID, adherence);
        assertEquals("forward:/", form);
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<Adherence> adherenceForm = ArgumentCaptor.forClass(Adherence.class);
    }
}
