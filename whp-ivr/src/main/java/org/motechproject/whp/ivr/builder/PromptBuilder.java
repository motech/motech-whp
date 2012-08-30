package org.motechproject.whp.ivr.builder;

import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WhpIvrMessage;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class PromptBuilder {

    private static final String ALPHANUMERIC_DIR = "alphanumeric";
    private static final String MESSAGES_DIR = "messages";

    private List<Prompt> prompts;
    private WhpIvrMessage whpIVRMessage;
    private String DEFAULT_LANGUAGE_CODE = "en";
    private static final String PATH_SEPARATOR = "/";

    public PromptBuilder(WhpIvrMessage whpIVRMessage) {
        this.whpIVRMessage = whpIVRMessage;
        prompts = new ArrayList<>();
    }

    public PromptBuilder number(Integer number) {
        prompts.add(newAudioPrompt(withFileURLFor(number)));
        return this;
    }

    public PromptBuilder id(String text) {
        text = text.toLowerCase();
        for (Character character : text.toCharArray()) {
            prompts.add(newAudioPrompt(withFileUrlFor(character)));
        }
        return this;
    }

    public PromptBuilder wav(String message) {
        prompts.add(newAudioPrompt(withFileUrlFor(message)));
        return this;
    }

    public PromptBuilder addAll(Prompt[] prompts) {
        this.prompts.addAll(asList(prompts));
        return this;
    }

    public Prompt[] build() {
        return prompts.toArray(new Prompt[prompts.size()]);
    }
    public List<Prompt> buildList() {
        return prompts;
    }

    private String withFileURLFor(Integer number) {
        return ALPHANUMERIC_DIR + PATH_SEPARATOR + number.toString();
    }

    private String withFileUrlFor(Character character) {
        return ALPHANUMERIC_DIR + PATH_SEPARATOR + character.toString();
    }

    private String withFileUrlFor(String message) {
        return MESSAGES_DIR + PATH_SEPARATOR + message;
    }

    private AudioPrompt newAudioPrompt(String fileName) {
        return new AudioPrompt().setAudioFileUrl(whpIVRMessage.getWav(fileName, DEFAULT_LANGUAGE_CODE));
    }
}
