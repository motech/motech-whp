package org.motechproject.whp.ivr.prompts;

import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.MUSIC_ENTER;
import static org.motechproject.whp.ivr.IvrAudioFiles.WELCOME_MESSAGE;

public class WelcomeMessagePrompts {

    public static Prompt[] welcomeMessagePrompts(WhpIvrMessage whpIvrMessage) {
       return new PromptBuilder(whpIvrMessage).wav(MUSIC_ENTER).wav(WELCOME_MESSAGE).build();
    }
}
