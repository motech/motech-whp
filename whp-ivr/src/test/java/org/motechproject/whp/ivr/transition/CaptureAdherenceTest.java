package org.motechproject.whp.ivr.transition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED;
import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE;

public class CaptureAdherenceTest {

    @Mock
    private AdherenceDataService adherenceDataService;

    private WHPIVRMessage whpivrMessage;

    private CaptureAdherence captureAdherence;
    private Node expectedNode;


    @Before
    public void setUp() {
        initMocks(this);
        whpivrMessage = new WHPIVRMessage(new Properties());
        captureAdherence = new CaptureAdherence(adherenceDataService, whpivrMessage);
    }

    @Test
    public void shouldReturnPromptWithPatientAdherenceInformation() {
        String providerId = "providerId";
        String preferredLanguage = "";

        List<String> allPatientsIds = asList("patient1", "patient2");
        List<String> patientsWithAdherence = asList("patient1", "patient2");

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId,
                allPatientsIds,
                patientsWithAdherence);

        when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(adherenceSummary);

        Prompt[] prompts = new PromptBuilder(whpivrMessage, preferredLanguage)
                .audio(NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithAdherence()))
                .audio(NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithoutAdherence()))
                .build();


        Node expectedNode = new Node().addPrompts(prompts);

        Node node = captureAdherence.getDestinationNode("");

        assertThat(node, is(expectedNode));
        verify(adherenceDataService, times(1)).getAdherenceSummary(providerId);
    }
}
