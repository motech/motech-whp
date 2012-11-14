package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CallCompletionPrompts {

    public static Prompt[] callCompletionPrompts(WhpIvrMessage whpIvrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE);

        return promptBuilder.build();
    }

    public static Prompt[] callCompletionPromptsAfterCapturingAdherence(WhpIvrMessage whpIvrMessage, Integer countOfAllPatients, Integer countOfPatientsWithAdherence) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(THANK_YOU);
        promptBuilder.addAll(
                adherenceSummaryWithCallCompletionPrompts(
                        whpIvrMessage,
                        countOfAllPatients,
                        countOfPatientsWithAdherence));
        return promptBuilder.build();
    }

    public static Prompt[] adherenceSummaryWithCallCompletionPrompts(WhpIvrMessage whpIvrMessage, Integer countOfAllPatients, Integer countOfPatientsWithAdherence) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR)
                .number(countOfAllPatients)
                .wav(END_OF_CALL_ADHERENCE_OUT_OF)
                .number(countOfPatientsWithAdherence)
                .wav(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS);

        if(countOfAllPatients > countOfPatientsWithAdherence){
            promptBuilder.wav(CALL_BACK_MESSAGE);
        }

        promptBuilder.addAll(callCompletionPrompts(whpIvrMessage));

        return promptBuilder.build();
    }
}

