package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceSummaryPromptsTest {

    private WHPIVRMessage whpivrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpivrMessage));
    }

    @Test
    public void shouldBuildAdherenceSummaryPrompts(){
        List<String> patientsWithoutAdherence = Arrays.asList("patient2", "patient3");
        List<String> patientsWithAdherence = Arrays.asList("patient1");

        Prompt[] builtPrompts = AdherenceSummaryPrompts.adherenceSummaryPrompts(whpivrMessage, patientsWithAdherence, patientsWithoutAdherence);
        Prompt[] expectedPrompts = promptBuilder.wav(ADHERENCE_PROVIDED_FOR).number(1).wav(ADHERENCE_TO_BE_PROVIDED_FOR).number(2).wav(ADHERENCE_CAPTURE_INSTRUCTION).build();

        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
