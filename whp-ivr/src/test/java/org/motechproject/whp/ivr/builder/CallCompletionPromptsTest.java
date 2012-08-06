package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.AudioPrompt;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CallCompletionPrompts;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;

public class CallCompletionPromptsTest {

    private WHPIVRMessage whpivrMessage;

    @Before
    public void setUp() throws Exception {
        whpivrMessage = new WHPIVRMessage(new Properties());
    }

    @Test
    public void shouldCreateCallCompletionPrompts() {
        Prompt[] prompts = CallCompletionPrompts.callCompletionPrompts(whpivrMessage);

        assertEquals(2, prompts.length);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[0]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[1]);
    }

    @Test
    public void shouldCreateCallCompletionPromptsWithAdherenceSummaryAndCallbackMessage_whenThereArePatientsWithoutAdherence() {
        List<Patient> allPatients = new ArrayList(createPatientsWithAdherence());
        Patient patientWithoutAdherence = new PatientBuilder()
                .withDefaults()
                .withPatientId("patientWithoutAdherence")
                .build();

        allPatients.add(patientWithoutAdherence);

        Integer countOfAllPatients = allPatients.size();
        Integer countOfPatientsWithAdherence = allPatients.size() - 1;

        Prompt[] prompts = adherenceSummaryWithCallCompletionPrompts(whpivrMessage, countOfAllPatients, countOfPatientsWithAdherence);

        assertEquals(8, prompts.length);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_PROVIDED_FOR), prompts[0]);
        assertEquals(audioPrompt(countOfPatientsWithAdherence.toString()), prompts[1]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_OUT_OF), prompts[2]);
        assertEquals(audioPrompt(countOfAllPatients.toString()), prompts[3]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS), prompts[4]);
        assertEquals(audioPrompt(CALL_BACK_MESSAGE), prompts[5]);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[6]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[7]);
    }

    @Test
    public void shouldCreateCallCompletionPromptsWithAdherenceSummary_whenThereAreNoPatientsWithoutAdherence() {
        List<Patient> allPatients = createPatientsWithAdherence();
        String numberOfPatients = String.valueOf(allPatients.size());

        Prompt[] prompts = adherenceSummaryWithCallCompletionPrompts(whpivrMessage, allPatients.size(), allPatients.size());

        assertEquals(7, prompts.length);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_PROVIDED_FOR), prompts[0]);
        assertEquals(audioPrompt(numberOfPatients), prompts[1]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_OUT_OF), prompts[2]);
        assertEquals(audioPrompt(numberOfPatients), prompts[3]);
        assertEquals(audioPrompt(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS), prompts[4]);
        assertEquals(audioPrompt(COMPLETION_MESSAGE), prompts[5]);
        assertEquals(audioPrompt(MUSIC_END_NOTE), prompts[6]);
    }

    private AudioPrompt audioPrompt(String audioFileUrl) {
        return new AudioPrompt().setAudioFileUrl(whpivrMessage.getWav(audioFileUrl, "en"));
    }


    @Test
    public void shouldPlayThankYouMessageAndAdherenceSummaryWithCallCompletionPrompts(){
        List<Patient> allPatients = createPatientsWithAdherence();
        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider("provider", allPatients);

        Prompt[] prompts = CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence(whpivrMessage,
                adherenceSummary.countOfAllPatients(),
                adherenceSummary.countOfPatientsWithAdherence());

        assertEquals(audioPrompt(THANK_YOU), prompts[0]);
        assertThat(asList(prompts), hasItems(adherenceSummaryWithCallCompletionPrompts(whpivrMessage, allPatients.size(), allPatients.size())));
    }

    private List<Patient> createPatientsWithAdherence() {
        return asList(
                new PatientBuilder()
                        .withDefaults()
                        .withPatientId("patientWithAdherence1")
                        .withAdherenceProvidedForLastWeek()
                        .build(),
                new PatientBuilder()
                        .withDefaults()
                        .withPatientId("patientWithAdherence2")
                        .withAdherenceProvidedForLastWeek()
                        .build(),
                new PatientBuilder()
                        .withDefaults()
                        .withPatientId("patientWithAdherence3")
                        .withAdherenceProvidedForLastWeek()
                        .build()
        );
    }

}
