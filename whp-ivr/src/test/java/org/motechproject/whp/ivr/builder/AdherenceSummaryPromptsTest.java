package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceSummaryPromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldBuildAdherenceSummaryPrompts(){
        List<String> patientsWithoutAdherence = Arrays.asList("patient2", "patient3");
        List<String> patientsWithAdherence = Arrays.asList("patient1");

        Prompt[] builtPrompts = AdherenceSummaryPrompts.adherenceSummaryPrompts(whpIvrMessage, patientsWithAdherence, patientsWithoutAdherence);
        Prompt[] expectedPrompts = promptBuilder.wav(ADHERENCE_PROVIDED_FOR).number(1).wav(ADHERENCE_TO_BE_PROVIDED_FOR).number(2).wav(ADHERENCE_CAPTURE_INSTRUCTION).build();

        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
