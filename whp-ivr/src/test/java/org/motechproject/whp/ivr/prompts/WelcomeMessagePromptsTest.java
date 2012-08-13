package org.motechproject.whp.ivr.prompts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.ivr.IvrAudioFiles.MUSIC_ENTER;
import static org.motechproject.whp.ivr.IvrAudioFiles.WELCOME_MESSAGE;

public class WelcomeMessagePromptsTest {
    @Mock
    WhpIvrMessage whpIvrMessage;

    @Before
    public void setUp(){
        initMocks(this);
    }
    @Test
    public void shouldGetWelcomePrompts() {
        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage).wav(MUSIC_ENTER).wav(WELCOME_MESSAGE).build();
        Prompt[] prompts = WelcomeMessagePrompts.welcomeMessagePrompts(whpIvrMessage);

        assertThat(prompts, is(expectedPrompts));
    }

}
