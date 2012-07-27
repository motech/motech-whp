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
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.ListUtils.sum;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
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

    private AdherenceSummaryTransition adherenceSummaryTransition;

    private WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    private FlowSession flowSession;

    @Before
    public void setUp() {
        initMocks(this);
        when(allProviders.findByPrimaryMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
        adherenceSummaryTransition = new AdherenceSummaryTransition(adherenceDataService, whpivrMessage, allProviders);
        flowSession = new FlowSessionStub();
        flowSession.set("cid", MOBILE_NUMBER);
    }

    @Test
    public void shouldAddPromptsForBothAdherenceSummaryAndAdherenceCapture_WhenThereArePatientsRemaining() {
        final List<String> patientsWithAdherence = asList("patient1");
        final List<String> patientsWithoutAdherence = asList("patient2", "patient3");

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(sum(patientsWithAdherence, patientsWithoutAdherence), patientsWithAdherence);
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        Node expectedNode = new Node()
                .addPrompts(adherenceSummaryPrompts(whpivrMessage, patientsWithAdherence, patientsWithoutAdherence))
                .addPrompts(CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, "patient2", 1))
                .addTransition("?", new AdherenceCaptureTransition());

        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertThat(actualNode, is(expectedNode));
    }

    @Test
    public void shouldAddPromptsForBothAdherenceSummaryAndCompletion_WhenThereAreNoPatientsRemaining() {
        List<String> allPatientsIds = asList("patient1", "patient2");
        List<String> patientsWithAdherence = asList("patient1", "patient2");
        List<String> patientsWithoutAdherence = new ArrayList<>();

        AdherenceSummaryByProvider adherenceSummary = adherenceSummary(allPatientsIds, patientsWithAdherence);
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID)).thenReturn(adherenceSummary);

        Node expectedNode = new Node()
                .addPrompts(adherenceSummaryPrompts(whpivrMessage, patientsWithAdherence, patientsWithoutAdherence))
                .addPrompts(callCompletionPrompts(whpivrMessage));

        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);
        assertThat(actualNode, is(expectedNode));
    }

    @Test
    public void shouldInitFlowSession() {
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary(asList("patient1", "patient2", "patient3"), asList("patient1")));
        FlowSessionStub flowSession = new FlowSessionStub();

        adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertEquals(asList("patient2", "patient3"), flowSession.get(IvrSession.PATIENTS_WITHOUT_ADHERENCE));
        assertEquals(PROVIDER_ID, flowSession.get("providerId"));
    }

    @Test
    public void shouldInitFlowSession_OnlyOnce() {
        when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                .thenReturn(adherenceSummary(asList("patient1", "patient2", "patient3"), asList("patient1")))
                .thenReturn(adherenceSummary(asList("patient1", "patient2", "patient3"), asList("patient1", "patient2")));
        FlowSessionStub flowSession = new FlowSessionStub();

        adherenceSummaryTransition.getDestinationNode("", flowSession);
        adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertEquals(asList("patient2", "patient3"), flowSession.get(IvrSession.PATIENTS_WITHOUT_ADHERENCE));
        assertEquals(PROVIDER_ID, flowSession.get("providerId"));
    }

    private AdherenceSummaryByProvider adherenceSummary(List<String> allPatientsIds, List<String> patientsWithAdherence) {
        return new AdherenceSummaryByProvider(PROVIDER_ID, allPatientsIds, patientsWithAdherence);
    }

}
