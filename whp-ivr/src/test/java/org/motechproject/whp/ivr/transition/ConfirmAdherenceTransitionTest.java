package org.motechproject.whp.ivr.transition;


import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts;
import org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.MenuRepeatFailurePrompts.noValidInputMovingOn;
import static org.motechproject.whp.ivr.session.IvrSession.*;

public class ConfirmAdherenceTransitionTest extends BaseUnitTest {

    static final String PATIENT1_ID = "patient1";
    static final String PATIENT2_ID = "patient2";

    Patient patient1;
    Patient patient2;
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    AdherenceDataService adherenceDataService;
    @Mock
    private ReportingPublisherService reportingPublisherService;

    FlowSession flowSession;
    WhpIvrMessage whpIvrMessage = new WhpIvrMessage(new Properties());
    ConfirmAdherenceTransition confirmAdherenceTransition;

    @Before
    public void setUp() {
        initMocks(this);
        patient1 = new PatientBuilder().withDefaults().withPatientId(PATIENT1_ID).build();
        patient2 = new PatientBuilder().withDefaults().withPatientId(PATIENT2_ID).build();

        flowSession = new FlowSessionStub();
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT1_ID, PATIENT2_ID)));
        flowSession.set(IvrSession.PROVIDER_ID, PROVIDER_ID);
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT1_ID).withAdherenceProvidedForLastWeek().build();
        when(patientService.findByPatientId(PATIENT1_ID)).thenReturn(patient);

        confirmAdherenceTransition = new ConfirmAdherenceTransition(whpIvrMessage, adherenceService, treatmentUpdateOrchestrator, reportingPublisherService, patientService, "2", "2");
    }

    @Test
    public void shouldAddRecordAdherenceOperation_ForValidInput() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, "3");
        Node node = confirmAdherenceTransition.getDestinationNode("1", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat((RecordAdherenceOperation) node.getOperations().get(0), is(new RecordAdherenceOperation(PATIENT1_ID, treatmentUpdateOrchestrator, reportingPublisherService)));
    }

    @Test
    public void shouldAddConfirmationPrompts_ForValidInput() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";

        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpIvrMessage, PATIENT2_ID, 2)));
    }

    @Test
    public void shouldReplayCurrentPatient_ForReEnterInput() {
        Node node = confirmAdherenceTransition.getDestinationNode("2", flowSession);

        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(PATIENT_LIST).number(1).id(PATIENT1_ID).wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build());
        assertThat(node.getPrompts(), is(expectedNode.getPrompts()));
    }

    @Test
    public void shouldIncrementPatientPosition() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);

        confirmAdherenceTransition.getDestinationNode("1", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
    }

    @Test
    public void shouldAddCallCompletionPrompts_WhenThereAreNoMorePatients() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";
        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpIvrMessage, PATIENT2_ID, 2)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
    }

    @Test
    public void shouldSetMaxDigitCountOnRepeating() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
        Node destinationNode = confirmAdherenceTransition.getDestinationNode("", flowSession);

        assertThat(destinationNode.getMaxTransitionInputDigit(), is(1));
    }

    @Test
    public void shouldSetMaxDigitCountOnSkippingCurrentPatient_ifHasNextPatient() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("", flowSession);

        assertThat(destinationNode.getMaxTransitionInputDigit(), is(1));
    }

    @Test
    public void shouldAddPublishCallLogOperation_WhenThereAreNoMorePatients() {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT1_ID)));
        flowSession.set(CURRENT_PATIENT_INDEX, 0);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";
        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);
        mockCurrentDate(DateUtil.now());
        DateTime now = DateUtil.now();

        assertThat(node.getOperations(), hasItem(new PublishCallLogOperation(reportingPublisherService, CallStatus.VALID_ADHERENCE_CAPTURE, now)));
    }

    @Test
    public void shouldRepeatOnNoInput_withinRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .addAll(ProvidedAdherencePrompts.providedAdherencePrompts(whpIvrMessage, PATIENT1_ID, adherenceInput, patient1.dosesPerWeek()))
                .addAll(ConfirmAdherencePrompts.confirmAdherencePrompts(whpIvrMessage)).build();

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("", flowSession);

        assertThat(destinationNode.getPrompts(), is(asList(expectedPrompts)));
        assertTrue(destinationNode.getTransitions().get("?") instanceof ConfirmAdherenceTransition);
    }

    @Test
    public void shouldRepeatOnInvalidInput_withinRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .addAll(ProvidedAdherencePrompts.providedAdherencePrompts(whpIvrMessage, PATIENT1_ID, adherenceInput, patient1.dosesPerWeek()))
                .addAll(ConfirmAdherencePrompts.confirmAdherencePrompts(whpIvrMessage)).build();

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("4", flowSession);

        assertThat(destinationNode.getPrompts(), is(asList(expectedPrompts)));
        assertTrue(destinationNode.getTransitions().get("?") instanceof ConfirmAdherenceTransition);
    }

    @Test
    public void shouldMoveToNextPatientOnNoInput_exceedingRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperationOnInvalidInput_exceedingRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("5", flowSession);

        assertThat(destinationNode.getOperations().size(), Matchers.is(0));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperationOnNoInput_exceedingRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("", flowSession);

        assertThat(destinationNode.getOperations().size(), Matchers.is(0));
    }

    @Test
    public void shouldSkipToNextPatientOnInvalidInput_exceedingRetryThreshold() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("5", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
    }

    @Test
    public void shouldMoveToNextPatientOnValidInputDuringRetry_afterInvalidInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("1", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
    }

    @Test
    public void shouldMoveToNextPatientOnValidInputDuringRetry_afterNoInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("1", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
    }

    @Test
    public void shouldResetRetryCountersOnTransitionToNextPatient() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
        assertEquals(0, ivrSession.currentInvalidInputRetryCount());
        assertEquals(0, ivrSession.currentNoInputRetryCount());
    }

    @Test
    public void shouldResetRetryCountersUponValidEntryDuringRepeat_afterInvalidEntry() {
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 1);
        confirmAdherenceTransition.getDestinationNode("2", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertEquals(0, ivrSession.currentInvalidInputRetryCount());
        assertEquals(0, ivrSession.currentNoInputRetryCount());
    }

    @Test
    public void shouldResetRetryCountersUponValidEntryDuringRepeat_afterNoEntry() {
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 1);
        confirmAdherenceTransition.getDestinationNode("2", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertEquals(0, ivrSession.currentInvalidInputRetryCount());
        assertEquals(0, ivrSession.currentNoInputRetryCount());
    }

    @Test
    public void shouldResetRetryCountersUponValidSkipEntry() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("2", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT1_ID));
        assertEquals(0, ivrSession.currentInvalidInputRetryCount());
        assertEquals(0, ivrSession.currentNoInputRetryCount());
    }

    @Test
    public void shouldResetRetryCountersUponValidConfirmationEntry() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        confirmAdherenceTransition.getDestinationNode("1", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT2_ID));
        assertEquals(0, ivrSession.currentInvalidInputRetryCount());
        assertEquals(0, ivrSession.currentNoInputRetryCount());
    }

    @Test
    public void shouldAddMenuRepeatFailurePromptsExceedingRetryThreshold_forInvalidInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        List<Prompt> prompts = confirmAdherenceTransition.getDestinationNode("5", flowSession).getPrompts();
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).addAll(noValidInputMovingOn(whpIvrMessage)).build();

        assertThat(prompts.get(0), is(expectedPrompts[0]));
    }

    @Test
    public void shouldAddMenuRepeatFailurePromptsExceedingRetryThreshold_forNoInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 2);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        List<Prompt> prompts = confirmAdherenceTransition.getDestinationNode("", flowSession).getPrompts();
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).addAll(noValidInputMovingOn(whpIvrMessage)).build();

        assertThat(prompts.get(0), is(expectedPrompts[0]));
    }

    @Test
    public void shouldNotAddMenuRepeatFailurePromptsWithinRetryThreshold_forInvalidInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        List<Prompt> prompts = confirmAdherenceTransition.getDestinationNode("5", flowSession).getPrompts();
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).addAll(noValidInputMovingOn(whpIvrMessage)).build();

        assertThat(prompts.get(0), not(is(expectedPrompts[0])));
        assertThat(prompts, not(contains(expectedPrompts)));
    }

    @Test
    public void shouldNotAddMenuRepeatFailurePromptsWithinRetryThreshold_forNoInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, 1);
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);

        List<Prompt> prompts = confirmAdherenceTransition.getDestinationNode("", flowSession).getPrompts();
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).addAll(noValidInputMovingOn(whpIvrMessage)).build();

        assertThat(prompts.get(0), not(is(expectedPrompts[0])));
        assertThat(prompts, not(contains(expectedPrompts)));
    }

}
