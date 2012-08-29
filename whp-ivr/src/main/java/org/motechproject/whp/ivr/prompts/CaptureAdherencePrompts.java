package org.motechproject.whp.ivr.prompts;


import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.session.IvrSession;

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

    public static Prompt[] captureAdherencePrompts(WhpIvrMessage whpIvrMessage, IvrSession ivrSession) {
        return captureAdherencePrompts(whpIvrMessage, ivrSession.currentPatientId(), ivrSession.currentPatientNumber());
    }

}

