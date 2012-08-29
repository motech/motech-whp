package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;

public class CaptureAdherencePromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldBuildCaptureAdherencePrompts(){
        Prompt[] builtPrompts = CaptureAdherencePrompts.captureAdherencePrompts(whpIvrMessage, "id", 2);
        Prompt[] expectedPrompts = promptBuilder.wav(PATIENT_LIST).number(2).id("i").id("d").wav(ENTER_ADHERENCE).build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
