package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class ConfirmAdherencePrompts {

    public static Prompt[] confirmAdherencePrompts(WhpIvrMessage whpIvrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(CONFIRM_ADHERENCE);
        return promptBuilder.build();
    }

}

