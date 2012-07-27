package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.prompts.CallCompletionPrompts;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.SavedAdherencePrompts.savedAdherencePrompts;
import static org.motechproject.whp.ivr.util.IvrSession.PATIENTS_WITHOUT_ADHERENCE;

public class AnotherAdherenceCaptureTransitionTest {

    public static final String PATIENT_1 = "patient1";
    public static final String PATIENT_2 = "patient2";
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;

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

        adherenceCaptureTransition = new AdherenceCaptureTransition(whpivrMessage, adherenceService, patientService);
    }

    @Test
    public void shouldAddRecordAdherenceOperation_ForValidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("3", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat((RecordAdherenceOperation) node.getOperations().get(0), is(new RecordAdherenceOperation(adherenceService, PATIENT_1)));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperation_ForInvalidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("8", flowSession);
        assertThat(node.getOperations().size(), is(0));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperation_ForSkipInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("9", flowSession);
        assertThat(node.getOperations().size(), is(0));
    }

    @Test
    public void shouldAddConfirmationPrompts_ForValidInput() {
        String adherenceInput = "3";
        int dosesPerWeek = 3;

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(savedAdherencePrompts(whpivrMessage, PATIENT_1, Integer.parseInt(adherenceInput), dosesPerWeek)));
    }

    @Test
    public void shouldNotAddConfirmationPrompts_ForSkippedInput() {
        String adherenceInput = "3";

        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1)));

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(callCompletionPrompts(whpivrMessage)));
    }

    @Test
    public void shouldIncrementPatientPosition() {
        String adherenceInput = "3";

        adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT_2));
    }

    @Test
    public void shouldAddPromptsAndTransitionsForNextPatient() {
        String adherenceInput = "3";
        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpivrMessage, PATIENT_2, 2)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
    }

    @Test
    public void shouldAddCallCompletionPrompts_WhenThereAreNoMorePatients() {
        String adherenceInput = "3";
        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpivrMessage, PATIENT_2, 2)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
    }

}
