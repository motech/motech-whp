package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class ProvidedAdherencePrompts {

    public static Prompt[] providedAdherencePrompts(WhpIvrMessage whpIvrMessage, int adherenceInput, int dosesPerWeek) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(PATIENT)
                .wav(HAS_TAKEN)
                .number(dosesPerWeek)
                .wav(OUT_OF)
                .number(adherenceInput)
                .wav(DOSES);
        return promptBuilder.build();
    }

}

