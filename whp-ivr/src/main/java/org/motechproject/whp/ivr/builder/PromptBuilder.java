package org.motechproject.whp.ivr.builder;

import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED;

public class PromptBuilder {

    private List<Prompt> prompts;
    private WHPIVRMessage whpIVRMessage;
    private String language;

    public PromptBuilder(WHPIVRMessage whpIVRMessage, String language) {
        this.whpIVRMessage = whpIVRMessage;
        this.language = language;
        prompts = new ArrayList<>();
    }

    public PromptBuilder audio(String message) {
        prompts.add(new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(message, language)));
        return this;
    }

    public Prompt[] build() {
        return prompts.toArray(new Prompt[prompts.size()]);
    }
}
