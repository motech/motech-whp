package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;

import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;

public class CaptureAdherencePrompts {

    public static Prompt[] captureAdherencePrompts(WhpIvrMessage whpIvrMessage, String patientId, int position) {
        PromptBuilder promptBuilder = new PromptBuilder(whpIvrMessage);
        promptBuilder.wav(PATIENT_LIST)
                .number(position)
                .id(patientId)
                .wav(ENTER_ADHERENCE);

        return promptBuilder.build();
    }
}

