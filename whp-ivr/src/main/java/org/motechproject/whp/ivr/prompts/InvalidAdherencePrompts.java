package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class InvalidAdherencePrompts {

    public static Prompt[] invalidAdherencePrompts(WhpIvrMessage whpIvrMessage, TreatmentCategory treatmentCategory) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);

        promptBuilder.wav(INVALID_ADHERENCE_MESSAGE_PART1);

        if (treatmentCategory.isGovernmentCategory()) {
            promptBuilder.wav(TREATMENT_CATEGORY_GOVT);
        } else {
            promptBuilder.wav(TREATMENT_CATEGORY_COMMERCIAL);
        }

        promptBuilder.wav(INVALID_ADHERENCE_MESSAGE_PART2)
                .number(0)
                .wav(INVALID_ADHERENCE_MESSAGE_PART3)
                .number(treatmentCategory.getDosesPerWeek())
                .wav(INVALID_ADHERENCE_MESSAGE_PART4);

        return promptBuilder.build();
    }


}

