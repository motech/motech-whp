package org.motechproject.whp.ivr.transition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;
import org.motechproject.whp.ivr.session.AdherenceRecordingSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

public class AdherenceSummaryTransitionTest {

    public static final String PROVIDER_ID = "providerid";
    public static final String MOBILE_NUMBER = "mobileNumber";

    @Mock
    private AdherenceDataService adherenceDataService;
    @Mock
    private AllProviders allProviders;
    private AdherenceRecordingSession recordingSession;

    private AdherenceSummaryTransition adherenceSummaryTransition;

    private WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    private FlowSession flowSession;
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;
    private Patient patient4;

    @Before
    public void setUp() {
        initMocks(this);
        when(allProviders.findByMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
        adherenceSummaryTransition = new AdherenceSummaryTransition(whpivrMessage, new AdherenceRecordingSession(allProviders, adherenceDataService));
        flowSession = new FlowSessionStub();
        flowSession.set("cid", MOBILE_NUMBER);

        patient1 = new PatientBuilder().withPatientId("patient1").build();
        patient2 = new PatientBuilder().withPatientId("patient2").build();
        patient3 = new PatientBuilder().withPatientId("patient3").build();
        patient4 = new PatientBuilder().withPatientId("patient4").build();
    }

    @Test
    public void shouldAddPromptsForBothAdherenceSummaryAndAdherenceCapture_WhenThereArePatientsRemaining() {
        final List<String> patientsWithAdherence = asList(patient1.getPatientId());
        final List<String> patientsWithoutAdherence = asList(patient2.getPatientId(), patient3.getPatientId());
        setAdherenceProvided(patient1);
        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(asList(patient1, patient2, patient3));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        Node expectedNode = new Node()
                .addPrompts(adherenceSummaryPrompts(whpivrMessage, patientsWithAdherence, patientsWithoutAdherence))
                .addPrompts(CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, patient2.getPatientId(), 1))
                .addTransition("?", new AdherenceCaptureTransition());

        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertThat(actualNode, is(expectedNode));
    }

    private Patient setAdherenceProvided(Patient patient) {
        patient.setLastAdherenceWeekStartDate(currentWeekInstance().startDate());
        return patient;
    }

    @Test
    public void shouldAddPromptsForBothAdherenceSummaryAndCompletion_WhenThereAreNoPatientsRemaining() {
        setAdherenceProvided(patient1);
        setAdherenceProvided(patient2);

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(asList(patient1, patient2));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        Node expectedNode = new Node()
                .addPrompts(adherenceSummaryPrompts(whpivrMessage, asList(patient1, patient2), emptyList()))
                .addPrompts(callCompletionPrompts(whpivrMessage));

        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertThat(actualNode, is(expectedNode));
    }

    @Test
    public void shouldInitFlowSession() {
        setAdherenceProvided(patient1);
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary(asList(patient1, patient2, patient3)));
        FlowSessionStub flowSession = new FlowSessionStub();

        adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertEquals(asList(patient2.getPatientId(), patient3.getPatientId()), flowSession.get(IvrSession.PATIENTS_WITHOUT_ADHERENCE));
        assertEquals(PROVIDER_ID, flowSession.get(IvrSession.PROVIDER_ID));
    }

    @Test
    public void shouldInitFlowSession_OnlyOnce() {
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary(asList(setAdherenceProvided(patient1), patient2, patient3)))
                .thenReturn(adherenceSummary(asList(patient1, setAdherenceProvided(patient2), patient3)));
        FlowSessionStub flowSession = new FlowSessionStub();

        adherenceSummaryTransition.getDestinationNode("", flowSession);
        adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertEquals(asList(patient2.getPatientId(), patient3.getPatientId()), flowSession.get(IvrSession.PATIENTS_WITHOUT_ADHERENCE));
        assertEquals(PROVIDER_ID, flowSession.get(IvrSession.PROVIDER_ID));
    }

    private AdherenceSummaryByProvider adherenceSummary(List<Patient> patients) {
        return new AdherenceSummaryByProvider(PROVIDER_ID, patients);
    }

}
