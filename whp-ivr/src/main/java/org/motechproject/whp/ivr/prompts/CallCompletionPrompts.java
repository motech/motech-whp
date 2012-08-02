package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.List;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;

public class CallCompletionPrompts {

    public static Prompt[] callCompletionPrompts(WHPIVRMessage whpivrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE);

        return promptBuilder.build();
    }

    public static Prompt[] callCompletionPromptsWithAdherenceSummary(WHPIVRMessage whpivrMessage, List patientsWithAdherence, List patientsWithoutAdherence) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.addAll(adherenceSummaryPrompts(whpivrMessage, patientsWithAdherence, patientsWithoutAdherence));

        if(patientsWithoutAdherence.size() > 0){
            promptBuilder.wav(CALL_BACK_MESSAGE);
        }
        promptBuilder.addAll(callCompletionPrompts(whpivrMessage));

        return promptBuilder.build();
    }
}

