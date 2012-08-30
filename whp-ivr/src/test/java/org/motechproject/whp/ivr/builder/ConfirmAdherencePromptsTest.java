package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts;

import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.CONFIRM_ADHERENCE;

public class ConfirmAdherencePromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldBuildConfirmAdherencePrompts() {
        Prompt[] builtPrompts = ConfirmAdherencePrompts.confirmAdherencePrompts(whpIvrMessage);
        Prompt[] expectedPrompts = promptBuilder.wav(CONFIRM_ADHERENCE).build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }

}
