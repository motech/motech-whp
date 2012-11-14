package org.motechproject.whp.ivr.builder;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.ivr.WhpIvrMessage;
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
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;

public class CallCompletionPromptsTest {

    private WhpIvrMessage whpIvrMessage;
    private PromptBuilder promptBuilder;

    @Before
    public void setUp() throws Exception {
        whpIvrMessage = new WhpIvrMessage(new Properties());
        promptBuilder = (new PromptBuilder(whpIvrMessage));
    }

    @Test
    public void shouldCreateCallCompletionPrompts() {
        Prompt[] builtPrompts = CallCompletionPrompts.callCompletionPrompts(whpIvrMessage);
        Prompt[] expectedPrompts = promptBuilder.wav(COMPLETION_MESSAGE).wav(MUSIC_END_NOTE).build();
        assertArrayEquals(expectedPrompts, builtPrompts);
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

        Prompt[] builtPrompts = adherenceSummaryWithCallCompletionPrompts(whpIvrMessage, countOfAllPatients, countOfPatientsWithAdherence);
        Prompt[] expectedPrompts = promptBuilder.wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR).number(countOfAllPatients)
                .wav(END_OF_CALL_ADHERENCE_OUT_OF).number(countOfPatientsWithAdherence)
                .wav(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS)
                .wav(CALL_BACK_MESSAGE)
                .wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE)
                .build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }

    @Test
    public void shouldCreateCallCompletionPromptsWithAdherenceSummary_whenThereAreNoPatientsWithoutAdherence() {
        List<Patient> allPatients = createPatientsWithAdherence();

        Prompt[] builtPrompts = adherenceSummaryWithCallCompletionPrompts(whpIvrMessage, allPatients.size(), allPatients.size());
        Prompt[] expectedPrompts = promptBuilder.wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR).number(allPatients.size())
                .wav(END_OF_CALL_ADHERENCE_OUT_OF).number(allPatients.size())
                .wav(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS)
                .wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE)
                .build();
        assertArrayEquals(expectedPrompts, builtPrompts);
    }


    @Test
    public void shouldPlayThankYouMessageAndAdherenceSummaryWithCallCompletionPrompts(){
        List<Patient> allPatients = createPatientsWithAdherence();
        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider("provider", allPatients);

        Prompt[] builtPrompts = CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence(whpIvrMessage,
                adherenceSummary.countOfAllPatients(),
                adherenceSummary.countOfPatientsWithAdherence());
        Prompt[] expectedPrompts = promptBuilder.wav(THANK_YOU).wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR).number(allPatients.size())
                .wav(END_OF_CALL_ADHERENCE_OUT_OF).number(allPatients.size())
                .wav(END_OF_CALL_ADHERENCE_TOTAL_PATIENTS)
                .wav(COMPLETION_MESSAGE)
                .wav(MUSIC_END_NOTE)
                .build();
        assertArrayEquals(expectedPrompts, builtPrompts);
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
