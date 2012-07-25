package org.motechproject.whp.ivr.transition;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceCaptureToCallCompleteTransitionTest {
    @Mock
    PatientService patientService;
    @Mock
    WHPAdherenceService adherenceService;
    @Mock
    FlowSession flowSession;

    WHPIVRMessage whpivrMessage;
    AdherenceCaptureToCallCompleteTransition adherenceCaptureToEndCallTransition;

    String patientId;
    String anotherPatientId;
    String providerId;

    @Before
    public void setUp() {
        initMocks(this);
        whpivrMessage = new WHPIVRMessage(new Properties());

        providerId = "providerid";
        patientId = "patientid";
        anotherPatientId = "someOtherPatientId";

        Patient patient = getPatientFor3DosesPerWeek(patientId);

        adherenceCaptureToEndCallTransition = new AdherenceCaptureToCallCompleteTransition(adherenceService, whpivrMessage, patientService);

        when(patientService.findByPatientId(patientId)).thenReturn(patient);
        when(flowSession.get(AdherenceSummaryToCaptureTransition.PATIENTS_WITHOUT_ADHERENCE)).thenReturn(new SerializableList(asList(patientId, anotherPatientId)));
    }

    @Test
    public void shouldSkipCurrentPatientIfKey9IsPressed() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(anotherPatientId)
                .wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureToCallCompleteTransition());

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("9", flowSession);
        assertEquals(expectedNode, destinationNode);

        when(flowSession.get("currentPatientPosition")).thenReturn(1);
    }

    @Test
    public void shouldSkipCurrentPatientIfEnteredDoseIsGreaterThanDosesPerWeek() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(anotherPatientId)
                .wav(ENTER_ADHERENCE);

        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureToCallCompleteTransition());

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("4", flowSession);
        assertEquals(expectedNode, destinationNode);
        verify(flowSession).set("currentPatientPosition", 1);
    }

    @Test
    public void shouldSkipCurrentPatientIfKeyPressedInNotNumber() {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage).wav(PATIENT_LIST)
                .number(2)
                .id(anotherPatientId)
                .wav(ENTER_ADHERENCE);
        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureToCallCompleteTransition());

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode("#", flowSession);
        assertEquals(expectedNode, destinationNode);
        verify(flowSession).set("currentPatientPosition", 1);
    }

    @Test
    public void shouldRecordAdherenceForValidInput() {
        int dosesTaken = 2;
        int dosesPerWeek = 3;
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage)
                .wav(CONFIRM_ADHERENCE)
                .id(patientId)
                .wav(HAS_TAKEN)
                .number(dosesTaken)
                .wav(OUT_OF)
                .number(dosesPerWeek)
                .wav(DOSES)
                .wav(PATIENT_LIST)
                .number(2)
                .id(anotherPatientId).wav(ENTER_ADHERENCE);

        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureToCallCompleteTransition());

        Node destinationNode = adherenceCaptureToEndCallTransition.getDestinationNode(String.valueOf(dosesTaken), flowSession);
        assertEquals(expectedNode, destinationNode);

        AuditParams auditParams = new AuditParams(providerId, AdherenceSource.IVR, "");

        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(patientId, currentWeekInstance());
        weeklyAdherenceSummary.setDosesTaken(2);
        verify(adherenceService, times(1)).recordAdherence(weeklyAdherenceSummary, auditParams);
    }

    @Test
    public void shouldHangUpIfPatientListIsEmpty() {
        Node expectedNode = new Node();

        when(flowSession.get(AdherenceSummaryToCaptureTransition.PATIENTS_WITHOUT_ADHERENCE)).thenReturn(new SerializableList(asList(patientId)));

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
