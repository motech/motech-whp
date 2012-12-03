package org.motechproject.whp.it.ivr.tree;

import org.junit.Test;
import org.motechproject.whp.it.ivr.util.KooKooIvrResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.it.ivr.matcher.IvrResponseAudioMatcher.audioList;

public class MenuRepetitionITPart extends IvrCallFlowITPart {

    @Test
    public void shouldRepeatAdherencePrompts_ForInvalidAdherenceInput_withinReplayThreshold() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("*");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(INVALID_ADHERENCE_MESSAGE_PART1, TREATMENT_CATEGORY_GOVT, INVALID_ADHERENCE_MESSAGE_PART2, "0", INVALID_ADHERENCE_MESSAGE_PART3, "3", INVALID_ADHERENCE_MESSAGE_PART4),
                wav(PATIENT_LIST, "1"), alphaNumeric(id(PATIENT_ID_1)), wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/*"))));
    }

    @Test
    public void shouldBeginAdherenceCaptureForNextPatient_ForInvalidAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("*");
        sendDtmf("*");
        sendDtmf("*");
        KooKooIvrResponse ivrResponse = sendDtmf("*");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id(PATIENT_ID_2)), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldRepeatAdherencePrompts_ForNoAdherenceInput_withinReplayThreshold() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "1"), alphaNumeric(id(PATIENT_ID_1)), wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/"))));
    }

    @Test
    public void shouldBeginAdherenceCaptureForNextPatient_ForNoAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("");
        sendDtmf("");
        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id(PATIENT_ID_2)), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldRepeatConfirmPrompts_ForInvalidAdherenceConfirmInput_withinMenuRepeatThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("3");
        KooKooIvrResponse ivrResponse = sendDtmf("9");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(wav(CONFIRM_ADHERENCE))));
        assertTrue(ivrResponse.getGotoUrl().contains((base64("/3/9"))));
    }

    @Test
    public void shouldRepeatConfirmPrompts_ForNoAdherenceConfirmInput_withinMenuRepeatThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("3");
        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(wav(CONFIRM_ADHERENCE))));
        assertTrue(ivrResponse.getGotoUrl().contains((base64("/3/"))));
    }

    @Test
    public void shouldSkipCurrentPatientAndProceedToCaptureNextPatientsAdherence_ForInvalidAdherenceConfirmInput_exceedingMenuRepeatThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("3");
        sendDtmf("3");
        sendDtmf("3");
        KooKooIvrResponse ivrResponse = sendDtmf("3");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id(PATIENT_ID_2)), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldSkipCurrentPatientAndProceedToCaptureNextPatientsAdherence_ForNoAdherenceConfirmInput_exceedingMenuRepeatThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("3");
        sendDtmf("");
        sendDtmf("");
        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id(PATIENT_ID_2)), wav(ENTER_ADHERENCE))));
    }
}
