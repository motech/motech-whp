package org.motechproject.whp.ivr.builder;

import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class PromptBuilder {

    private List<Prompt> prompts;
    private WHPIVRMessage whpIVRMessage;
    private String DEFAULT_LANGUAGE_CODE = "en";

    public PromptBuilder(WHPIVRMessage whpIVRMessage) {
        this.whpIVRMessage = whpIVRMessage;
        prompts = new ArrayList<>();
    }

    public PromptBuilder number(Integer number) {
        prompts.add(new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(number.toString(), DEFAULT_LANGUAGE_CODE)));
        return this;
    }

    public PromptBuilder id(String text) {
        text = text.toLowerCase();
        for (Character character : text.toCharArray()) {
            prompts.add(new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(character.toString(), DEFAULT_LANGUAGE_CODE)));
        }
        return this;
    }

    public PromptBuilder wav(String message) {
        prompts.add(new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(message, "en")));
        return this;
    }

    public PromptBuilder addAll(Prompt[] prompts) {
        this.prompts.addAll(asList(prompts));
        return this;
    }

    public Prompt[] build() {
        return prompts.toArray(new Prompt[prompts.size()]);
    }

}
