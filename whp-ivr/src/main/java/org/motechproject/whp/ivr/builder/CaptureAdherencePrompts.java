package org.motechproject.whp.ivr.builder;


import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import static org.motechproject.whp.ivr.IvrAudioFiles.ENTER_ADHERENCE;
import static org.motechproject.whp.ivr.IvrAudioFiles.PATIENT_LIST;

public class CaptureAdherencePrompts {

    public static Prompt[] captureAdherencePrompts(WHPIVRMessage whpivrMessage, String patientId, int position) {
        PromptBuilder promptBuilder = new PromptBuilder(whpivrMessage);
        promptBuilder.wav(PATIENT_LIST)
                .number(position)
                .id(patientId)
                .wav(ENTER_ADHERENCE);

        return promptBuilder.build();
    }
}

