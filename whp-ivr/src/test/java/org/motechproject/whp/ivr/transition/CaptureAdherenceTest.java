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
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED;
import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

public class CaptureAdherenceTest {

    @Mock
    private AdherenceDataService adherenceDataService;
    @Mock
    private AllProviders allProviders;

    private WHPIVRMessage whpivrMessage = new WHPIVRMessage(new Properties());
    private Node expectedNode;
    private CaptureAdherence captureAdherence;

    @Before
    public void setUp() {
        initMocks(this);
        captureAdherence = new CaptureAdherence(adherenceDataService, whpivrMessage, allProviders);
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
                .audio(NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithAdherence()))
                .audio(NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithoutAdherence()))
                .build();


        Node expectedNode = new Node().addPrompts(prompts);

        FlowSession flowSession = mock(FlowSession.class);

        Node node = captureAdherence.getDestinationNode("", flowSession);

        assertThat(node, is(expectedNode));
        verify(adherenceDataService, times(1)).getAdherenceSummary(providerId);
    }
}
