package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class ProvidedAdherencePrompts {

    public static Prompt[] providedAdherencePrompts(WHPIVRMessage whpivrMessage, String patientId, int adherenceInput, int dosesPerWeek) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(PATIENT)
                .id(patientId)
                .wav(HAS_TAKEN)
                .number(dosesPerWeek)
                .wav(OUT_OF)
                .number(adherenceInput)
                .wav(DOSES)
                .wav(CONFIRM_ADHERENCE);
        return promptBuilder.build();
    }

}

