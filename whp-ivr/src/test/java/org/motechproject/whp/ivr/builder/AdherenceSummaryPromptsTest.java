package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.ADHERENCE_CAPTURE_INSTRUCTION;
import static org.motechproject.whp.ivr.IvrAudioFiles.ADHERENCE_PROVIDED_FOR;
import static org.motechproject.whp.ivr.IvrAudioFiles.ADHERENCE_TO_BE_PROVIDED_FOR;

public class AdherenceSummaryPromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldBuildAdherenceSummaryPrompts(){
        List<String> allPatients = Arrays.asList("patient1", "patient2", "patient3");
        List<String> patientsWithAdherence = Arrays.asList("patient1");

        AdherenceSummaryByProvider adherenceSummaryByProvider = new AdherenceSummaryByProvider("providerId",
                allPatients,
                patientsWithAdherence);

        Prompt[] prompts = AdherenceSummaryPrompts.adherenceSummary(whpivrMessage, adherenceSummaryByProvider);

        assertEquals(5, prompts.length);
        assertEquals(audioPrompt(ADHERENCE_PROVIDED_FOR), prompts[0]);
        assertEquals(audioPrompt("1"), prompts[1]);
        assertEquals(audioPrompt(ADHERENCE_TO_BE_PROVIDED_FOR), prompts[2]);
        assertEquals(audioPrompt("2"), prompts[3]);
        assertEquals(audioPrompt(ADHERENCE_CAPTURE_INSTRUCTION), prompts[4]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }
}
