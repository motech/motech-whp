package org.motechproject.whp.ivr.builder;

import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.ArrayList;
import java.util.List;

public class PromptBuilder {

    private List<Prompt> prompts;
    private WHPIVRMessage whpIVRMessage;

    public PromptBuilder(WHPIVRMessage whpIVRMessage) {
        this.whpIVRMessage = whpIVRMessage;
        prompts = new ArrayList<>();
    }

    public PromptBuilder audio(String message) {
        prompts.add(new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(message, "en")));
        return this;
    }

    public Prompt[] build() {
        return prompts.toArray(new Prompt[prompts.size()]);
    }
}
