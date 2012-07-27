package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import java.util.List;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class AdherenceSummaryPrompts {

    public static Prompt[] adherenceSummaryPrompts(WHPIVRMessage whpivrMessage, List patientsWithAdherence, List patientsWithoutAdherence) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);

        promptBuilder.wav(ADHERENCE_PROVIDED_FOR)
                .number(patientsWithAdherence.size())
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(patientsWithoutAdherence.size())
                .wav(ADHERENCE_CAPTURE_INSTRUCTION);
        return promptBuilder.build();
    }

}

