package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;

public class CaptureAdherencePromptsTest {

    private WHPIVRMessage whpivrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpivrMessage));
    }

    @Test
    public void shouldBuildCaptureAdherencePrompts(){
        Prompt[] builtPrompts = CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, "id", 2);
        Prompt[] expectedPrompts = promptBuilder.wav(PATIENT_LIST).number(2).id("i").id("d").wav(ENTER_ADHERENCE).build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
