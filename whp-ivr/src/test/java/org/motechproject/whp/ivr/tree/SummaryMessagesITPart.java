package org.motechproject.whp.ivr.tree;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.ivr.util.KooKooIvrResponse;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.matcher.IvrResponseAudioMatcher.audioList;

public class SummaryMessagesITPart extends IvrCallFlowITPart {

    @Test
    public void shouldPlayWelcomeMessage() {
        KooKooIvrResponse ivrResponse = startCall(provider.getPrimaryMobile());
        List<String> playAudioList = ivrResponse.getPlayAudio();
        assertThat(playAudioList, audioList(wav(MUSIC_ENTER, WELCOME_MESSAGE)));
    }

    @Test
    public void shouldPlayAdherenceSummary_uponCallBeginning_withinAdherenceCaptureWindow() {
        KooKooIvrResponse ivrResponse = startCall(provider.getPrimaryMobile());
        assertThat(ivrResponse.getPlayAudio(),
                is(audioList(
                        wav(ADHERENCE_PROVIDED_FOR),
                        alphaNumeric("0"),
                        wav(ADHERENCE_TO_BE_PROVIDED_FOR),
                        alphaNumeric("3"),
                        wav(ADHERENCE_CAPTURE_INSTRUCTION),
                        wav(PATIENT_LIST),
                        alphaNumeric("1"),
                        alphaNumeric(id(PATIENT_ID_1)),
                        wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldPlayAdherenceSummaryWhenProviderHasProvidedAdherenceForAllPatients() {
        startCall(provider.getPrimaryMobile());
        recordConfirmedAdherence("2");
        recordConfirmedAdherence("3");
        recordConfirmedAdherence("2");

        KooKooIvrResponse ivrResponse = startCall(provider.getPrimaryMobile());

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(END_OF_CALL_ADHERENCE_PROVIDED_FOR, "3", END_OF_CALL_ADHERENCE_OUT_OF, "3", END_OF_CALL_ADHERENCE_TOTAL_PATIENTS, COMPLETION_MESSAGE, MUSIC_END_NOTE))));
        assertThat(ivrResponse.callEnded(), is(true));
    }

    @Test
    public void shouldPlayWindowClosedPrompt_IfNotInAdherenceCaptureWindow() throws IOException {
        LocalDate thursday = currentAdherenceCaptureWeek().startDate().plusDays(3);
        adjustDateTime(thursday);

        KooKooIvrResponse ivrResponse = startCall(provider.getPrimaryMobile());

        assertThat(ivrResponse.getPlayAudio(), is(audioList(wav(WINDOW_OVER, THANK_YOU))));
        assertThat(ivrResponse.callEnded(), is(true));
    }
}
