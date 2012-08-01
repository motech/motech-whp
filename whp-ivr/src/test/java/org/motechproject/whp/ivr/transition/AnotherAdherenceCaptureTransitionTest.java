package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;
import static org.motechproject.whp.ivr.util.IvrSession.PATIENTS_WITHOUT_ADHERENCE;

public class AnotherAdherenceCaptureTransitionTest {

    public static final String PATIENT_1 = "patient1";
    public static final String PATIENT_2 = "patient2";
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    FlowSession flowSession;
    WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    AdherenceCaptureTransition adherenceCaptureTransition;

    @Before
    public void setUp() {
        initMocks(this);
        flowSession = new FlowSessionStub();
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1, PATIENT_2)));
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_1).build();
        when(patientService.findByPatientId(PATIENT_1)).thenReturn(patient);

        adherenceCaptureTransition = new AdherenceCaptureTransition(whpivrMessage, adherenceService, phaseUpdateOrchestrator, patientService);
    }

    @Test
    public void shouldAddConfirmAdherenceOperation_ForValidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("3", flowSession);
        assertThat(node.getOperations().size(), is(2));
        assertThat(node.getOperations().get(0), instanceOf(GetAdherenceOperation.class));
        assertThat(node.getOperations().get(1), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldNotAddConfirmAdherenceOperation_ForInvalidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("8", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat(node.getOperations().get(0), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldNotAddConfirmAdherenceOperation_ForSkipInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("9", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat(node.getOperations().get(0), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldAddConfirmationPrompts_ForValidInput() {
        String adherenceInput = "3";
        int dosesPerWeek = 3;

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(providedAdherencePrompts(whpivrMessage, PATIENT_1, Integer.parseInt(adherenceInput), dosesPerWeek)));
    }

    @Test
    public void shouldNotAddConfirmationPrompts_ForSkippedInput() {
        String adherenceInput = "7";

        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1)));

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(callCompletionPrompts(whpivrMessage)));
        assertThat(node.getPrompts(), not(hasItems(providedAdherencePrompts(whpivrMessage, PATIENT_1, Integer.parseInt(adherenceInput), 3))));
    }

    @Test
    public void shouldAddCaptureAdherencePromptsAndTransitionForNextPatientOnInValidInput() {
        String adherenceInput = "7";
        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpivrMessage, PATIENT_2, 2)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
    }

    @Test
    public void shouldAddTransitionForConfirmAdherenceOnValidInput() {
        String adherenceInput = "3";
        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getTransitions().size(), is(1));
        assertThat(node.getTransitions().get("?"), instanceOf(ConfirmAdherenceTransition.class));
    }

}
