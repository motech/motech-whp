package org.motechproject.whp.ivr.transition;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence;
import static org.motechproject.whp.ivr.session.IvrSession.PROVIDER_ID;
import static org.motechproject.whp.ivr.util.PlatformStub.play;
import static org.motechproject.whp.ivr.util.PlatformStub.replay;

public class AdherenceCaptureTransitionTest extends BaseUnitTest {

    @Mock
    PatientService patientService;

    @Mock
    AdherenceDataService adherenceDataService;

    FlowSession flowSession;
    WHPIVRMessage whpivrMessage;
    AdherenceCaptureTransition adherenceCaptureTransition;

    String providerId = "providerid";

    String patientId1 = "patientid";
    String patientId2 = "someOtherPatientId";

    @Before
    public void setUp() {
        initMocks(this);
        whpivrMessage = new WHPIVRMessage(new Properties());
        flowSession = new FlowSessionStub();
        flowSession.set(IvrSession.PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(patientId1, patientId2)));
        flowSession.set(IvrSession.PROVIDER_ID, PROVIDER_ID);

        Patient patient = getPatientFor3DosesPerWeek(patientId1);

        adherenceCaptureTransition = new AdherenceCaptureTransition(whpivrMessage, adherenceDataService, patientService);

        when(patientService.findByPatientId(patientId1)).thenReturn(patient);
    }

    @Test
    public void shouldSkipCurrentPatientIfKey9IsPressed() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(patientId2)
                .wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureTransition());

        Node destinationNode = adherenceCaptureTransition.getDestinationNode("9", flowSession);
        assertEquals(expectedNode, destinationNode);
    }

    @Test
    public void shouldSkipCurrentPatientIfEnteredDoseIsGreaterThanDosesPerWeek() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(patientId2)
                .wav(ENTER_ADHERENCE);

        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureTransition());

        Node destinationNode = adherenceCaptureTransition.getDestinationNode("4", flowSession);

        assertEquals(expectedNode, destinationNode);
        assertEquals(1, flowSession.get(IvrSession.CURRENT_PATIENT_INDEX));
    }

    @Test
    public void shouldSkipCurrentPatientIfKeyPressedInNotNumber() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(patientId2)
                .wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureTransition());

        Node destinationNode = adherenceCaptureTransition.getDestinationNode("#", flowSession);
        assertEquals(expectedNode, destinationNode);
        assertEquals(1, flowSession.get(IvrSession.CURRENT_PATIENT_INDEX));
    }

    @Test
    public void shouldRecordAdherenceForValidInput() {
        int dosesTaken = 2;
        int dosesPerWeek = 3;
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage)
                .wav(PATIENT)
                .id(patientId1)
                .wav(HAS_TAKEN)
                .number(dosesTaken)
                .wav(OUT_OF)
                .number(dosesPerWeek)
                .wav(DOSES)
                .wav(CONFIRM_ADHERENCE);

        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new ConfirmAdherenceTransition());

        Node destinationNode = adherenceCaptureTransition.getDestinationNode(String.valueOf(dosesTaken), flowSession);
        assertEquals(expectedNode, destinationNode);
    }

    @Test
    public void shouldHangUpWithCallCompletionSummaryIfPatientListIsEmpty() {
        Patient patient = new PatientBuilder().withPatientId("patient1").withAdherenceProvidedForLastWeek().build();

        flowSession.set(IvrSession.PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(patientId1)));
        flowSession.set(IvrSession.PATIENTS_WITH_ADHERENCE, new SerializableList(asList(patient.getPatientId())));

        Node expectedNode = new Node().addPrompts(callCompletionPromptsAfterCapturingAdherence(whpivrMessage,
                2, 1));

        Node destinationNode = adherenceCaptureTransition.getDestinationNode("9", flowSession);
        assertEquals(expectedNode, destinationNode);
    }

    @Test
    public void shouldBuildNodeThatResetsAdherenceSubmissionTimeIfPatientDoseIsSkipped() {
        DateTime now = new DateTime(2011, 1, 1, 1, 1, 1, 1);
        mockCurrentDate(now);

        new IvrSession(flowSession).startOfAdherenceSubmission(now.minusDays(5));
        play(adherenceCaptureTransition, flowSession, "9");
        Assert.assertEquals(now, new IvrSession(flowSession).startOfAdherenceSubmission());
    }

    @Test
    public void shouldBuildNodeThatDoesNotResetAdherenceSubmissionTimeIfPatientDoseIsInvalid() {
        DateTime now = new DateTime(2011, 1, 1, 1, 1, 1, 1);
        mockCurrentDate(now);

        DateTime oldTime = now.minusDays(5);
        new IvrSession(flowSession).startOfAdherenceSubmission(oldTime);
        play(adherenceCaptureTransition, flowSession, "8");
        Assert.assertEquals(oldTime, new IvrSession(flowSession).startOfAdherenceSubmission());
    }

    @Test
    public void shouldBuildNodeThatDoesNotResetAdherenceSubmissionTimeIfPatientDoseIsValid() {
        DateTime now = new DateTime(2011, 1, 1, 1, 1, 1, 1);
        mockCurrentDate(now);

        DateTime oldTime = now.minusDays(5);
        new IvrSession(flowSession).startOfAdherenceSubmission(oldTime);
        play(adherenceCaptureTransition, flowSession, "1");
        Assert.assertEquals(oldTime, new IvrSession(flowSession).startOfAdherenceSubmission());
    }

    @Test
    public void shouldNotResetAdherenceSubmissionTimeWhenReplayed() {
        DateTime now = new DateTime(2011, 1, 1, 1, 1, 1, 1);
        mockCurrentDate(now);

        DateTime oldTime = now.minusDays(5);
        new IvrSession(flowSession).startOfAdherenceSubmission(oldTime);
        replay(adherenceCaptureTransition, flowSession, "1");
        Assert.assertEquals(oldTime, new IvrSession(flowSession).startOfAdherenceSubmission());
    }

    private Patient getPatientFor3DosesPerWeek(String patientId) {
        TreatmentCategory treatmentCategory = new TreatmentCategory();
        treatmentCategory.setDosesPerWeek(3);
        Therapy therapy = new Therapy(treatmentCategory, DiseaseClass.P, 20);
        Patient patient = new Patient(patientId, "", "", Gender.F, "");
        Treatment treatment = new TreatmentBuilder().withProviderId(providerId).build();
        patient.addTreatment(treatment, therapy, DateUtil.now());
        return patient;
    }

}
