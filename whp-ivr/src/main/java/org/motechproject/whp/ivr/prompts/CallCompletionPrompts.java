package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CallCompletionPrompts {

    public static Prompt[] callCompletionPrompts(WHPIVRMessage whpivrMessage) {

        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(CALL_BACK_MESSAGE)
                .wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE);

        return promptBuilder.build();
    }
}

