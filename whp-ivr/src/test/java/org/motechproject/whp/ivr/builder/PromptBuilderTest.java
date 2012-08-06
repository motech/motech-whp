package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;


public class PromptBuilderTest {

    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.setProperty(WHPIVRMessage.CONTENT_LOCATION_URL, "http://content-url/");
    }

    @Test
    public void shouldBuildAudioPromptsWithNumber() {
        PromptBuilder promptBuilder = new PromptBuilder(new WHPIVRMessage(properties));
        assertEquals("http://content-url/en/1.wav", ((AudioPrompt) promptBuilder.number(1).build()[0]).getAudioFileUrl());
    }

    @Test
    public void shouldBuildAudioPromptsWithId() {
        PromptBuilder promptBuilder = new PromptBuilder(new WHPIVRMessage(properties));
        Prompt[] prompts = promptBuilder.id("1a").build();
        assertEquals(2, prompts.length);
        assertEquals("http://content-url/en/1.wav", ((AudioPrompt) prompts[0]).getAudioFileUrl());
        assertEquals("http://content-url/en/a.wav", ((AudioPrompt) prompts[1]).getAudioFileUrl());
    }

    @Test
    public void shouldBuildAudioPromptsWithIdInUppercase() {
        PromptBuilder promptBuilder = new PromptBuilder(new WHPIVRMessage(properties));
        Prompt[] prompts = promptBuilder.id("1A").build();
        assertEquals(2, prompts.length);
        assertEquals("http://content-url/en/1.wav", ((AudioPrompt) prompts[0]).getAudioFileUrl());
        assertEquals("http://content-url/en/a.wav", ((AudioPrompt) prompts[1]).getAudioFileUrl());
    }


}