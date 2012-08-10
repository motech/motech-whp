package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts;

import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class ConfirmAdherencePromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldBuildConfirmAdherencePrompts(){
        Prompt[] builtPrompts = ProvidedAdherencePrompts.providedAdherencePrompts(whpIvrMessage, "id", 2, 3);
        Prompt[] expectedPrompts = promptBuilder.wav(PATIENT).id("i").id("d")
                .wav(HAS_TAKEN).number(3)
                .wav(OUT_OF).number(2)
                .wav(DOSES).wav(CONFIRM_ADHERENCE)
                .build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
