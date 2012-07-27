package org.motechproject.whp.ivr.transition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

public class AdherenceSummaryTransitionTest {

    @Mock
    private AdherenceDataService adherenceDataService;
    @Mock
    private AllProviders allProviders;

    private WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    private AdherenceSummaryTransition adherenceSummaryTransition;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceSummaryTransition = new AdherenceSummaryTransition(adherenceDataService, whpivrMessage, allProviders);
    }

    @Test
    public void shouldReturnPromptWithPatientAdherenceInformation() {
        String providerId = "providerid";
        String mobileNumber = "mobileNumber";

        List<String> allPatientsIds = asList("patient1", "patient2");
        List<String> patientsWithAdherence = asList("patient1", "patient2");
        FlowSession flowSession = mock(FlowSession.class);

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId,
                allPatientsIds,
                patientsWithAdherence);

        when(flowSession.get("cid")).thenReturn(mobileNumber);
        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(adherenceSummary);
        when(allProviders.findByPrimaryMobileNumber(mobileNumber)).thenReturn(newProviderBuilder().withProviderId(providerId).build());

        Prompt[] prompts = getPromptBuilder(patientsWithAdherence, adherenceSummary).build();
        Node expectedNode = new Node().addPrompts(prompts);
        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertThat(actualNode, is(expectedNode));
        verify(adherenceDataService, times(1)).getAdherenceSummary(providerId);
    }

    private PromptBuilder getPromptBuilder(List<String> patientsWithAdherence, AdherenceSummaryByProvider adherenceSummary) {
        return new PromptBuilder(whpivrMessage)
                .wav(ADHERENCE_PROVIDED_FOR)
                .number(patientsWithAdherence.size())
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(adherenceSummary.countOfPatientsWithoutAdherence())
                .wav(ADHERENCE_CAPTURE_INSTRUCTION);
    }

    @Test
    public void shouldListPatientsAndGetAdherence() {
        String providerId = "providerid";
        String mobileNumber = "mobileNumber";

        List<String> allPatientsIds = asList("patient1", "patient2", "patient3");
        List<String> patientsWithAdherence = asList("patient1");
        FlowSession flowSession = new FlowSessionStub();
        flowSession.set("cid", mobileNumber);
        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId, allPatientsIds, patientsWithAdherence);

        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(adherenceSummary);
        when(allProviders.findByPrimaryMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(providerId).build());

        PromptBuilder promptBuilder = getPromptBuilder(patientsWithAdherence, adherenceSummary)
                .wav(PATIENT_LIST)
                .number(1)
                .id("patient2")
                .wav(ENTER_ADHERENCE);

        Node expectedNode = new Node().addPrompts(promptBuilder.build())
                .addTransition("?", new AdherenceCaptureTransition());
        Node actualNode = adherenceSummaryTransition.getDestinationNode("", flowSession);

        assertThat(actualNode, is(expectedNode));
    }

}
