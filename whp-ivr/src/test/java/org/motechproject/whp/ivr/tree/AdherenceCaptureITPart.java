package org.motechproject.whp.ivr.tree;

import org.junit.Test;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.ivr.util.KooKooIvrResponse;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.matcher.IvrResponseAudioMatcher.audioList;

public class AdherenceCaptureITPart extends IvrCallFlowITPart {
    @Test
    public void shouldAskForConfirmation_UponEnteringValidAdherenceValue() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("2");

        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertThat(adherenceSummaryPatient1.getDosesTaken(), is(0));

        assertThat(ivrResponse.getPlayAudio(),
                is(audioList(
                        wav(PATIENT),
                        alphaNumeric(id(patient1.getPatientId())),
                        wav(HAS_TAKEN, "3", OUT_OF, "2", DOSES, CONFIRM_ADHERENCE))));

        assertThat(ivrResponse.getGotoUrl(), is("http://localhost:7080/whp/kookoo/ivr?provider=kookoo&ln=en&tree=adherenceCapture&trP=" + base64("/2")));
    }

    @Test
    public void shouldRecordAdherenceForPatient_uponEnteringValidAdherenceValueAndConfirmation() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = recordConfirmedAdherence("2");
        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "2"),
                alphaNumeric(id(PATIENT_ID_2)),
                wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains(base64("/2/1")));
        assertThat(adherenceService.currentWeekAdherence(patient1).getDosesTaken(), is(2));
        verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
    }

    @Test
    public void shouldSkipSavingAdherence_uponSkippingCurrentPatient_ByPressing9() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("9");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "2"),
                alphaNumeric(id(PATIENT_ID_2)),
                wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/9"))));
    }

    @Test
    public void shouldRecordAdherenceForMultiplePatients() {
        startCall(provider.getPrimaryMobile());
        recordConfirmedAdherence("2");
        recordConfirmedAdherence("3");
        KooKooIvrResponse ivrResponse = recordConfirmedAdherence("2");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(THANK_YOU, END_OF_CALL_ADHERENCE_PROVIDED_FOR, "3", END_OF_CALL_ADHERENCE_OUT_OF, "3", END_OF_CALL_ADHERENCE_TOTAL_PATIENTS, COMPLETION_MESSAGE, MUSIC_END_NOTE))));

        assertNull(ivrResponse.getGotoUrl());
        assertThat(ivrResponse.callEnded(), is(true));
    }
}
