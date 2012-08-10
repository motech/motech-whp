package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.InvalidAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetFlowSessionOperation;
import org.motechproject.whp.ivr.prompts.InvalidAdherencePrompts;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;
import static org.motechproject.whp.ivr.session.IvrSession.*;

public class AnotherAdherenceCaptureTransitionTest {

    public static final String PATIENT_1 = "patient1";
    public static final String PATIENT_2 = "patient2";
    Patient patient1;
    Patient patient2;
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    AdherenceDataService adherenceDataService;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    private ReportingPublisherService reportingPublisherService;

    FlowSession flowSession;
    WhpIvrMessage whpIvrMessage = new WhpIvrMessage(new Properties());
    AdherenceCaptureTransition adherenceCaptureTransition;

    @Before
    public void setUp() {
        initMocks(this);
        patient1 = new PatientBuilder().withDefaults().withPatientId(PATIENT_1).build();
        patient2 = new PatientBuilder().withDefaults().withPatientId(PATIENT_2).build();

        flowSession = new FlowSessionStub();
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1, PATIENT_2)));
        flowSession.set(IvrSession.PROVIDER_ID, PROVIDER_ID);
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_1).build();
        when(patientService.findByPatientId(PATIENT_1)).thenReturn(patient);

        adherenceCaptureTransition = new AdherenceCaptureTransition(whpIvrMessage, patientService, reportingPublisherService);
    }

    @Test
    public void shouldAddConfirmAdherenceOperation_ForValidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("3", flowSession);

        assertThat(node.getOperations().size(), is(2));
        assertThat(node.getOperations().get(0), instanceOf(GetAdherenceOperation.class));
        assertThat(node.getOperations().get(1), instanceOf(ResetFlowSessionOperation.class));
    }

    @Test
    public void shouldNotAddConfirmAdherenceOperation_ForInvalidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("8", flowSession);
        assertThat(node.getOperations(), hasItem(not(isA(ResetFlowSessionOperation.class))));
    }

    @Test
    public void shouldAddInvalidAdherenceOperation_ForInvalidInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("8", flowSession);
        assertThat(node.getOperations(), hasItem(isA(InvalidAdherenceOperation.class)));
    }

    @Test
    public void shouldNotAddConfirmAdherenceOperation_ForSkipInput() {
        Node node = adherenceCaptureTransition.getDestinationNode("9", flowSession);
        assertThat(node.getOperations(), hasItem(isA(ResetFlowSessionOperation.class)));
    }

    @Test
    public void shouldAddConfirmationPrompts_ForValidInput() {
        String adherenceInput = "3";
        int dosesPerWeek = 3;

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        assertThat(node.getPrompts(), hasItems(providedAdherencePrompts(whpIvrMessage, PATIENT_1, Integer.parseInt(adherenceInput), dosesPerWeek)));
    }

    @Test
    public void shouldNotAddConfirmationPrompts_ForSkippedInput() {
        String adherenceInput = "9";

        Patient patientWithAdherence = new PatientBuilder().withPatientId("patient1").withAdherenceProvidedForLastWeek().build();

        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(PATIENT_1)));
        flowSession.set(PATIENTS_WITH_ADHERENCE, new SerializableList(asList(patientWithAdherence.getPatientId())));

        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);
        Prompt[] expectedPrompts = callCompletionPromptsAfterCapturingAdherence(whpIvrMessage, 2, 1);
        assertThat(node.getPrompts().size(), is(expectedPrompts.length));
        assertThat(node.getPrompts(), hasItems(expectedPrompts));
    }

    @Test
    public void shouldAddTransitionForConfirmAdherenceOnValidInput() {
        String adherenceInput = "3";
        Node node = adherenceCaptureTransition.getDestinationNode(adherenceInput, flowSession);

        assertThat(node.getTransitions().size(), is(1));
        assertThat(node.getTransitions().get("?"), instanceOf(ConfirmAdherenceTransition.class));
    }

    @Test
    public void shouldAddInvalidAdherencePrompts_OnWrongInput() {

        Node node = adherenceCaptureTransition.getDestinationNode("8", flowSession);

        assertThat(node.getTransitions().size(), is(1));
        assertThat(node.getTransitions().get("?"), instanceOf(AdherenceCaptureTransition.class));

        Prompt[] expectedPrompts = InvalidAdherencePrompts.invalidAdherencePrompts(whpIvrMessage, patient1.getCurrentTherapy().getTreatmentCategory());
        assertThat(node.getPrompts(), hasItems(expectedPrompts));
    }

    @Test
    public void shouldAddCaptureAdherencePromptsAndTransitionForCurrentPatient_OnWrongInput() {
        flowSession.set(CURRENT_PATIENT_INDEX, 0);
        Prompt[] expectedPrompts = captureAdherencePrompts(whpIvrMessage, new IvrSession(flowSession));

        Node node = adherenceCaptureTransition.getDestinationNode("4", flowSession);

        assertThat(node.getPrompts(), hasItems(expectedPrompts));
        assertThat(node.getTransitions().size(), is(1));
        assertThat((AdherenceCaptureTransition) node.getTransitions().get("?"), is(new AdherenceCaptureTransition()));
        assertThat((Integer)flowSession.get(CURRENT_PATIENT_INDEX), is(0));
    }

}
