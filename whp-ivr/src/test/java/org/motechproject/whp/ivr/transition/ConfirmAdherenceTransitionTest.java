package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsWithAdherenceSummary;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.util.IvrSession.CURRENT_PATIENT_ADHERENCE_INPUT;
import static org.motechproject.whp.ivr.util.IvrSession.PATIENTS_WITHOUT_ADHERENCE;
import static org.motechproject.whp.ivr.util.IvrSession.PROVIDER_ID;

public class ConfirmAdherenceTransitionTest {

    public static final String PATIENT_1 = "patient1";
    public static final String PATIENT_2 = "patient2";
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    ReportingPublisherService reportingService;
    @Mock
    AdherenceDataService adherenceDataService;


    FlowSession flowSession;
    WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    ConfirmAdherenceTransition confirmAdherenceTransition;
    private AdherenceSummaryByProvider adherenceSummary;

    @Before
    public void setUp() {
        initMocks(this);
        flowSession = new FlowSessionStub();
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1, PATIENT_2)));
        flowSession.set(IvrSession.PROVIDER_ID, PROVIDER_ID);
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_1).build();
        when(patientService.findByPatientId(PATIENT_1)).thenReturn(patient);

        adherenceSummary = new AdherenceSummaryByProvider(PROVIDER_ID, asList("patient1"), asList("patient1"));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary);

        confirmAdherenceTransition = new ConfirmAdherenceTransition(whpivrMessage, adherenceService, treatmentUpdateOrchestrator, patientService, reportingService, adherenceDataService);
    }

    @Test
    public void shouldAddRecordAdherenceOperation_ForValidInput() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, "3");
        Node node = confirmAdherenceTransition.getDestinationNode("1", flowSession);
        assertThat(node.getOperations().size(), is(2));
        assertThat((RecordAdherenceOperation) node.getOperations().get(0), is(new RecordAdherenceOperation(PATIENT_1, treatmentUpdateOrchestrator, reportingService)));
        assertThat(node.getOperations().get(1), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperation_ForInvalidInput() {
        Node node = confirmAdherenceTransition.getDestinationNode("8", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat(node.getOperations().get(0), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldNotAddRecordAdherenceOperation_ForSkipInput() {
        Node node = confirmAdherenceTransition.getDestinationNode("9", flowSession);
        assertThat(node.getOperations().size(), is(1));
        assertThat(node.getOperations().get(0), instanceOf(ResetPatientIndexOperation.class));
    }

    @Test
    public void shouldAddConfirmationPrompts_ForValidInput() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";

        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpivrMessage, PATIENT_2, 2)));
    }

    @Test
    public void shouldEndCallPatient_ForLastPatientOnInvalidInput() {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1)));

        Node node = confirmAdherenceTransition.getDestinationNode("5", flowSession);
        Node expectedNode = new Node().addPrompts(callCompletionPromptsWithAdherenceSummary(whpivrMessage, adherenceSummary.getAllPatientsWithAdherence(), adherenceSummary.getAllPatientsWithoutAdherence()));
        assertThat(node.getPrompts(), is(expectedNode.getPrompts()));
    }

    @Test
    public void shouldAskAdherenceForNextPatient_ForInvalidInput() {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1, PATIENT_2)));

        Node node = confirmAdherenceTransition.getDestinationNode("5", flowSession);
        Node expectedNode = new Node().addPrompts(CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, PATIENT_2, 2));
        assertThat(node.getPrompts(), is(expectedNode.getPrompts()));
    }

    @Test
    public void shouldReplayCurrentPatient_ForReEnterInput() {
        Node node = confirmAdherenceTransition.getDestinationNode("2", flowSession);

        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(PATIENT_LIST).number(1).id(PATIENT_1).wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build());
        assertThat(node.getPrompts(), is(expectedNode.getPrompts()));
    }

    @Test
    public void shouldIncrementPatientPosition() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);

        confirmAdherenceTransition.getDestinationNode("1", flowSession);

        IvrSession ivrSession = new IvrSession(flowSession);
        assertThat(ivrSession.currentPatientId(), is(PATIENT_2));
    }

    @Test
    public void shouldAddCallCompletionPrompts_WhenThereAreNoMorePatients() {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, 2);
        String adherenceInput = "1";
        Node node = confirmAdherenceTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getPrompts(), hasItems(captureAdherencePrompts(whpivrMessage, PATIENT_2, 2)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
    }

}
