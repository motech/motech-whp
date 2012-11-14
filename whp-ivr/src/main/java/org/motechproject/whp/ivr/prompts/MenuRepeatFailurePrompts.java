package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class MenuRepeatFailurePrompts {

    public static Prompt[] noValidInputMovingOn(WhpIvrMessage whpIvrMessage) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);

        promptBuilder.wav(MENU_REPEAT_FAILURE);

        return promptBuilder.build();
    }
}

