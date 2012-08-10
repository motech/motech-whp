package org.motechproject.whp.ivr.prompts;


import org.junit.Before;
import org.junit.Test;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.ivr.IvrAudioFiles;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class InvalidAdherencePromptsTest {

    private WhpIvrMessage whpIvrMessage;

    @Before
    public void setUp() {
        whpIvrMessage = new WhpIvrMessage(new Properties());
    }

    @Test
    public void shouldGetInvalidAdherencePromptsForPatientOnGovernmentCategory() {
        List<DayOfWeek> threeDaysAWeek = asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek);

        Prompt[] prompts = InvalidAdherencePrompts.invalidAdherencePrompts(whpIvrMessage, treatmentCategory);

        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .wav(INVALID_ADHERENCE_MESSAGE_PART1)
                .wav(TREATMENT_CATEGORY_GOVT)
                .wav(INVALID_ADHERENCE_MESSAGE_PART2)
                .number(0)
                .wav(INVALID_ADHERENCE_MESSAGE_PART3)
                .number(3)
                .wav(INVALID_ADHERENCE_MESSAGE_PART4)
                .build();
        assertThat(prompts, is(expectedPrompts));
    }

    @Test
    public void shouldGetInvalidAdherencePromptsForPatientOnCommercialCategory() {
        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, asList(DayOfWeek.values()));

        Prompt[] prompts = InvalidAdherencePrompts.invalidAdherencePrompts(whpIvrMessage, treatmentCategory);

        Prompt[] expectedPrompts = new PromptBuilder(whpIvrMessage)
                .wav(INVALID_ADHERENCE_MESSAGE_PART1)
                .wav(TREATMENT_CATEGORY_COMMERCIAL)
                .wav(INVALID_ADHERENCE_MESSAGE_PART2)
                .number(0)
                .wav(INVALID_ADHERENCE_MESSAGE_PART3)
                .number(7)
                .wav(INVALID_ADHERENCE_MESSAGE_PART4)
                .build();
        assertThat(prompts, is(expectedPrompts));
    }

}
