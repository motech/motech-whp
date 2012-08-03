package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CallCompletionPrompts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CallCompletionPromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldCreateCallCompletionPrompts(){
        Prompt[] prompts = CallCompletionPrompts.callCompletionPrompts(whpivrMessage);

        assertEquals(2, prompts.length);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[0]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[1]);
    }

    @Test
    public void shouldCreateCallCompletionPromptsWithAdherenceSummaryAndCallbackMessage_whenThereArePatientsWithoutAdherence(){
        List<String> patientsWithAdherence = Arrays.asList("patient1");
        List<String> allPatients = Arrays.asList("patient1","patient2", "patient3");

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider("provider", allPatients, patientsWithAdherence);

        Prompt[] prompts = CallCompletionPrompts.callCompletionPromptsWithAdherenceSummary(whpivrMessage, adherenceSummary);

        assertEquals(8, prompts.length);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_PROVIDED_FOR), prompts[0]);
        assertEquals(audioPrompt("1"), prompts[1]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_OUT_OF), prompts[2]);
        assertEquals(audioPrompt("3"), prompts[3]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS), prompts[4]);
        assertEquals(audioPrompt(CALL_BACK_MESSAGE), prompts[5]);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[6]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[7]);
    }

    @Test
    public void shouldCreateCallCompletionPromptsWithAdherenceSummary_whenThereAreNoPatientsWithoutAdherence(){
        List<String> patientsWithAdherence = Arrays.asList("patient1");
        List<String> allPatients = Arrays.asList("patient1");

        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider("provider", allPatients, patientsWithAdherence);

        Prompt[] prompts = CallCompletionPrompts.callCompletionPromptsWithAdherenceSummary(whpivrMessage, adherenceSummary);

        assertEquals(7, prompts.length);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_PROVIDED_FOR), prompts[0]);
        assertEquals(audioPrompt("1"), prompts[1]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_OUT_OF), prompts[2]);
        assertEquals(audioPrompt("1"), prompts[3]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS), prompts[4]);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[5]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[6]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }


}
