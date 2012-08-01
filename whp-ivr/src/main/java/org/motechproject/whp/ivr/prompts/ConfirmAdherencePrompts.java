package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.CONFIRM_ADHERENCE;

public class ConfirmAdherencePrompts {

    public static Prompt[] confirmAdherencePrompts(WHPIVRMessage whpivrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(CONFIRM_ADHERENCE);

        return promptBuilder.build();
    }
}
