package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceCaptureWindowClosedPrompts {

    public static Prompt[] adherenceCaptureWindowClosedPrompts(WHPIVRMessage whpivrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(WINDOW_OVER)
        .wav(THANK_YOU);

        return promptBuilder.build();
    }
}

