package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
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

public class AdherenceControllerTest {

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
        patient = PatientBuilder.startRecording().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldShowTreatmentForm() {
        String form = adherenceController.update("patientId", uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToTreatmentForm() {
        adherenceController.update("patientId", uiModel);
        verify(uiModel).addAttribute(eq("patientId"), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToTreatmentForm() {
        adherenceController.update("patientId", uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(3, captors.adherenceForm.getValue().getAdherenceLogs().size());
    }

    @Test
    public void shouldCaptureAdherence() {
        Adherence adherence = new Adherence(DateUtil.today(), asList(Monday));
        adherenceController.update("patientId", adherence, uiModel);
        verify(adherenceService).recordAdherence("patientId", adherence);
    }

    @Test
    public void shouldShowTreatmentFormAfterCapturingAdherence() {
        Adherence adherence = new Adherence(DateUtil.today(), asList(Monday));
        String form = adherenceController.update("patientId", adherence, uiModel);
        assertEquals("adherence/update", form);
    }

    @Test
    public void shouldPassPatientIdToTreatmentFormAfterCapturingAdherence() {
        Adherence adherence = new Adherence(DateUtil.today(), asList(Monday));
        adherenceController.update("patientId", adherence, uiModel);
        verify(uiModel).addAttribute(eq("patientId"), eq(patient.getPatientId()));
    }

    @Test
    public void shouldPassWeeklyAdherenceLogToTreatmentFormAfterCapturingAdherence() {
        Adherence adherence = new Adherence(DateUtil.today(), asList(Monday));
        adherenceController.update("patientId", adherence, uiModel);

        verify(uiModel).addAttribute(eq("adherence"), captors.adherenceForm.capture());
        assertEquals(3, captors.adherenceForm.getValue().getAdherenceLogs().size());
    }

    private class ArgumentCaptors {
        private ArgumentCaptor<Adherence> adherenceForm = ArgumentCaptor.forClass(Adherence.class);
    }
}
