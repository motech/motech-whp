package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.List;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceSummaryPrompts {

    public static Prompt[] adherenceSummaryPrompts(WhpIvrMessage whpIvrMessage, List patientsWithAdherence, List patientsWithoutAdherence) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(ADHERENCE_PROVIDED_FOR)
                .number(patientsWithAdherence.size())
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(patientsWithoutAdherence.size())
                .wav(ADHERENCE_CAPTURE_INSTRUCTION);
        return promptBuilder.build();
    }

}

