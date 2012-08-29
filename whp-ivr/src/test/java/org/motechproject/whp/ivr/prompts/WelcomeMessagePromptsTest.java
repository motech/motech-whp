package org.motechproject.whp.ivr.prompts;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.ivr.IvrAudioFiles.MUSIC_ENTER;
import static org.motechproject.whp.ivr.IvrAudioFiles.WELCOME_MESSAGE;

public class WelcomeMessagePromptsTest {

    WhpIvrMessage whpIvrMessage;

    @Before
    public void setUp() {
        whpIvrMessage = new WhpIvrMessage(new Properties());
    }

    @Test
    public void shouldGetWelcomePrompts() {
        Prompt[] prompts = WelcomeMessagePrompts.welcomeMessagePrompts(whpIvrMessage);

        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).wav(MUSIC_ENTER).wav(WELCOME_MESSAGE).build();
        assertThat(prompts, is(expectedPrompts));
    }

}
