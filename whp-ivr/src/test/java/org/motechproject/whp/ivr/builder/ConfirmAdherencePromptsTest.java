package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class ConfirmAdherencePromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldBuildConfirmAdherencePrompts(){
        Prompt[] prompts = ProvidedAdherencePrompts.providedAdherencePrompts(whpivrMessage, "id", 2, 3);

        assertEquals(9, prompts.length);
        assertEquals(audioPrompt(PATIENT), prompts[0]);
        assertEquals(audioPrompt("i"), prompts[1]);
        assertEquals(audioPrompt("d"), prompts[2]);
        assertEquals(audioPrompt(HAS_TAKEN), prompts[3]);
        assertEquals(audioPrompt("2"), prompts[4]);
        assertEquals(audioPrompt(OUT_OF), prompts[5]);
        assertEquals(audioPrompt("3"), prompts[6]);
        assertEquals(audioPrompt(DOSES), prompts[7]);
        assertEquals(audioPrompt(CONFIRM_ADHERENCE), prompts[8]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }
}
