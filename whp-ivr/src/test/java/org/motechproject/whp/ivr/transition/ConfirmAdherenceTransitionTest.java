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

import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherencePrompts;
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

        confirmAdherenceTransition = new ConfirmAdherenceTransition(whpIvrMessage, adherenceService, treatmentUpdateOrchestrator, reportingPublisherService, patientService);
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
    public void shouldRepeatConfirmOrReenterPrompts_ForInvalidInput() {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT1_ID, PATIENT2_ID)));
        Patient patient = PatientBuilder.patient();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);

        Node node = confirmAdherenceTransition.getDestinationNode("5", flowSession);

        assertThat(node.getPrompts(), is(asList(confirmAdherencePrompts(whpIvrMessage))));
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
    public void shouldAddPublishCallLogOperation_WhenThereAreNoMorePatients() {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT1_ID)));
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";
        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);
        mockCurrentDate(DateUtil.now());
        DateTime now = DateUtil.now();

        assertThat(node.getOperations(), hasItem(new PublishCallLogOperation(reportingPublisherService, CallStatus.VALID_ADHERENCE_CAPTURE, now)));
    }

    @Test
    public void shouldRepeatOnNoInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .addAll(ProvidedAdherencePrompts.providedAdherencePrompts(whpIvrMessage, PATIENT1_ID, adherenceInput, patient1.dosesPerWeek()))
                .addAll(ConfirmAdherencePrompts.confirmAdherencePrompts(whpIvrMessage)).build();

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("", flowSession);

        assertThat(destinationNode.getPrompts(), hasItems(expectedPrompts));
        assertThat(destinationNode.getPrompts().size(), Matchers.is(expectedPrompts.length));
        assertTrue(destinationNode.getTransitions().get("?") instanceof ConfirmAdherenceTransition);
    }

    @Test
    public void shouldRepeatOnInvalidInput() {
        int adherenceInput = 2;
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, adherenceInput);
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .addAll(ConfirmAdherencePrompts.confirmAdherencePrompts(whpIvrMessage)).build();

        Node destinationNode = confirmAdherenceTransition.getDestinationNode("4", flowSession);

        assertThat(destinationNode.getPrompts(), hasItems(expectedPrompts));
        assertThat(destinationNode.getPrompts().size(), Matchers.is(expectedPrompts.length));
        assertTrue(destinationNode.getTransitions().get("?") instanceof ConfirmAdherenceTransition);
    }

}
