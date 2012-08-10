package org.motechproject.whp.ivr.transition;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.operation.RecordCallStartTimeOperation;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;
import org.motechproject.whp.ivr.session.AdherenceRecordingSession;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.ivr.prompts.AdherenceCaptureWindowClosedPrompts.adherenceCaptureWindowClosedPrompts;
import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;


public class AdherenceSummaryTransitionTest extends BaseUnitTest {

    public static final String PROVIDER_ID = "providerid";
    public static final String MOBILE_NUMBER = "mobileNumber";

    @Mock
    private AdherenceDataService adherenceDataService;
    @Mock
    private AllProviders allProviders;
    @Mock
    private ReportingPublisherService reportingPublisherService;

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
        LocalDate sunday = new LocalDate(2012, 7, 15);
        mockCurrentDate(sunday);
        when(allProviders.findByMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
        adherenceSummaryTransition = new AdherenceSummaryTransition(whpivrMessage, new AdherenceRecordingSession(allProviders, adherenceDataService), reportingPublisherService);

        flowSession = new FlowSessionStub();
        flowSession.set("cid", MOBILE_NUMBER);

        patient1 = new PatientBuilder().withPatientId("patient1").withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).build();
        patient2 = new PatientBuilder().withPatientId("patient2").withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).build();
        patient3 = new PatientBuilder().withPatientId("patient3").withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).build();
        patient4 = new PatientBuilder().withPatientId("patient4").withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).build();
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
        patient.setLastAdherenceWeekStartDate(currentAdherenceCaptureWeek().startDate());
        return patient;
    }

    @Test
    public void shouldAddPromptsForBothAdherenceSummaryAndCompletion_WhenThereAreNoPatientsRemaining() {
        setAdherenceProvided(patient1);
        setAdherenceProvided(patient2);

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(asList(patient1, patient2));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);
        mockCurrentDate(DateUtil.now());

        PublishCallLogOperation publishCallLogOperation = new PublishCallLogOperation(reportingPublisherService, now());
        Node expectedNode = new Node()
                .addPrompts(adherenceSummaryWithCallCompletionPrompts(whpivrMessage,
                        adherenceSummary.countOfAllPatients(),
                        adherenceSummary.countOfPatientsWithAdherence()))
                .addOperations(publishCallLogOperation);

        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertThat(actualNode, is(expectedNode));
        assertThat(actualNode.getOperations(),  hasItem(publishCallLogOperation));
    }

    @Test
    public void shouldBuildNodeWhichCapturesTimeOfAdherenceSubmission() {
        DateTime now = new DateTime(2011, 1, 2, 1, 1, 1, 1);
        mockCurrentDate(now);

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(asList(patient1, patient2));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        List<INodeOperation> operations = adherenceSummaryTransition.getDestinationNode("", flowSession).getOperations();
        for (INodeOperation operation : operations) {
            operation.perform("", flowSession);
        }
        assertEquals(now, new IvrSession(flowSession).startOfAdherenceSubmission());
    }

    @Test
    public void shouldAddRecordCallStartTime() throws Exception {
        DateTime now = new DateTime(2011, 1, 2, 1, 1, 1, 1);
        mockCurrentDate(now);

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(asList(patient1, patient2));
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        List<INodeOperation> operations = adherenceSummaryTransition.getDestinationNode("", flowSession).getOperations();
        assertThat(operations, hasItem(new RecordCallStartTimeOperation(now)));
    }
    
    @Test
    public void shouldPlayWindowClosedPrompts_whenCalledBetweenWedToSat() {
        DateTime now = new DateTime(2012, 8, 8, 1, 1, 1, 1);
        mockCurrentDate(now);

        Node destinationNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertEquals(asList(adherenceCaptureWindowClosedPrompts(whpivrMessage)), destinationNode.getPrompts());
        assertThat(destinationNode.getTransitions().keySet(), is(empty()));
    }

    private AdherenceSummaryByProvider adherenceSummary(List<Patient> patients) {
        return new AdherenceSummaryByProvider(PROVIDER_ID, patients);
    }

}
