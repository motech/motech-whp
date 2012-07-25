package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CallCompletionPrompts;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CallCompletionPromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldCreateCallCompletionPrompts(){
        Prompt[] prompts = CallCompletionPrompts.callCompletionPrompts(whpivrMessage);

        assertEquals(3, prompts.length);
        assertEquals(audioPrompt(CALL_BACK_MESSAGE), prompts[0]);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[1]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[2]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }


}
