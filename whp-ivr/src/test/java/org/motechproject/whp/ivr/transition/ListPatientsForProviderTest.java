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
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.transition.ListPatientsForProvider.*;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

public class ListPatientsForProviderTest {

    @Mock
    private AdherenceDataService adherenceDataService;
    @Mock
    private AllProviders allProviders;

    private WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    private Node expectedNode;
    private ListPatientsForProvider listPatientsForProvider;

    @Before
    public void setUp() {
        initMocks(this);
        listPatientsForProvider = new ListPatientsForProvider(adherenceDataService, whpivrMessage, allProviders);
    }

    @Test
    public void shouldReturnPromptWithPatientAdherenceInformation() {
        String providerId = "providerid";
        String mobileNumber = "mobileNumber";

        List<String> allPatientsIds = asList("patient1", "patient2");
        List<String> patientsWithAdherence = asList("patient1", "patient2");

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId,
                allPatientsIds,
                patientsWithAdherence);

        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(adherenceSummary);
        when(allProviders.findByPrimaryMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(providerId).withPrimaryMobileNumber(mobileNumber).build());

        Prompt[] prompts = new PromptBuilder(whpivrMessage)
                .wav(ADHERENCE_PROVIDED_FOR)
                .number(patientsWithAdherence.size())
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(adherenceSummary.countOfPatientsWithoutAdherence())
                .wav(ADHERENCE_CAPTURE_INSTRUCTION)
                .build();


        Node expectedNode = new Node().addPrompts(prompts);

        FlowSession flowSession = mock(FlowSession.class);

        Node node = listPatientsForProvider.getDestinationNode("", flowSession);

        assertThat(node, is(expectedNode));
        verify(adherenceDataService, times(1)).getAdherenceSummary(providerId);
    }

    @Test
    public void shouldListPatientsWithoutAdherenceAndGetAdherence() {
        String providerId = "providerid";
        String mobileNumber = "mobileNumber";

        List<String> allPatientsIds = asList("patient1", "patient2", "patient3");
        List<String> patientsWithAdherence = asList("patient1");

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId,
                allPatientsIds,
                patientsWithAdherence);

        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(adherenceSummary);
        when(allProviders.findByPrimaryMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(providerId).withPrimaryMobileNumber(mobileNumber).build());

        Prompt[] prompts = new PromptBuilder(whpivrMessage).text("patient2").build();


        FlowSession flowSession = mock(FlowSession.class);

        Node node = listPatientsForProvider.getDestinationNode("", flowSession);

        assertTrue(node.getPrompts().containsAll(asList(prompts)));
        assertThat(node.getTransitions().size(), is(1));
        assertThat(node.getTransitions().get("?"), is(AdherenceCapture.class));

        verify(flowSession, times(1)).set("patientsWithoutAdherence", new SerializableList(asList("patient3")));
    }

}
