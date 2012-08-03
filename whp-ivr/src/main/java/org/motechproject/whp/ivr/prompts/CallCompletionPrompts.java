package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.List;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CallCompletionPrompts {

    public static Prompt[] callCompletionPrompts(WHPIVRMessage whpivrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE);

        return promptBuilder.build();
    }

    public static Prompt[] callCompletionPromptsWithAdherenceSummary(WHPIVRMessage whpivrMessage, AdherenceSummaryByProvider adherenceSummary) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR)
                .number(adherenceSummary.countOfPatientsWithAdherence())
                .wav(END_OF_CALL_ADHERENCE_OUT_OF)
                .number(adherenceSummary.countOfAllPatients())
                .wav(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS);

        if(adherenceSummary.hasPatientsWithoutAdherence()){
            promptBuilder.wav(CALL_BACK_MESSAGE);
        }

        promptBuilder.addAll(callCompletionPrompts(whpivrMessage));

        return promptBuilder.build();
    }
}

