package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.prompts.CallCompletionPrompts;
import org.motechproject.whp.ivr.prompts.MenuRepeatFailurePrompts;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;

public class MenuRepeatFailurePromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldCreateMenuRepeatFailurePrompts() {
        Prompt[] builtPrompts = MenuRepeatFailurePrompts.noValidInputMovingOn(whpIvrMessage);
        Prompt[] expectedPrompts = promptBuilder.wav(MENU_REPEAT_FAILURE).build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }
}
