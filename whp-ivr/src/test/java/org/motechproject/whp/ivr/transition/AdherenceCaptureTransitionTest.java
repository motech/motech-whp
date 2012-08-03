package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.session.IvrSession;
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
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsWithAdherenceSummary;
import static org.motechproject.whp.ivr.session.IvrSession.PROVIDER_ID;

public class AdherenceCaptureTransitionTest {

    @Mock
    PatientService patientService;

    @Mock
    AdherenceDataService adherenceDataService;

    FlowSession flowSession;
    WHPIVRMessage whpivrMessage;
    AdherenceCaptureTransition adherenceCaptureToEndCallTransition;

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

        adherenceCaptureToEndCallTransition = new AdherenceCaptureTransition(whpivrMessage, adherenceDataService, patientService);

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

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("9", flowSession);
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

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("4", flowSession);

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

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("#", flowSession);
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

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode(String.valueOf(dosesTaken), flowSession);
        assertEquals(expectedNode, destinationNode);
    }

    @Test
    public void shouldHangUpWithCallCompletionSummaryIfPatientListIsEmpty() {
        Patient patient = new PatientBuilder().withPatientId("patient1").withAdherenceProvidedForLastWeek().build();
        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(PROVIDER_ID, asList(patient));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary);

        Node expectedNode = new Node().addPrompts(callCompletionPromptsWithAdherenceSummary(whpivrMessage,
                adherenceSummary));

        flowSession.set(IvrSession.PATIENTS_WITHOUT_ADHERENCE, new SerializableList(asList(patientId1)));

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("9", flowSession);
        assertEquals(expectedNode, destinationNode);
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
