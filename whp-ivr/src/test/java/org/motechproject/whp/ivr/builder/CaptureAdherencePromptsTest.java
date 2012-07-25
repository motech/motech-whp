package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;

public class CaptureAdherencePromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldBuildCaptureAdherencePrompts(){
        Prompt[] prompts = CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, "id", 2);

        assertEquals(5, prompts.length);
        assertEquals(audioPrompt(PATIENT_LIST), prompts[0]);
        assertEquals(audioPrompt("2"), prompts[1]);
        assertEquals(audioPrompt("i"), prompts[2]);
        assertEquals(audioPrompt("d"), prompts[3]);
        assertEquals(audioPrompt(ENTER_ADHERENCE), prompts[4]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }
}
