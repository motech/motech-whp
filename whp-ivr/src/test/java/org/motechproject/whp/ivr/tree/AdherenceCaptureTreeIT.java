package org.motechproject.whp.ivr.tree;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.util.KooKooIvrResponse;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.ivr.IvrAudioFiles.*;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.*;
import static org.motechproject.whp.ivr.matcher.IvrResponseAudioMatcher.audioList;

public class AdherenceCaptureTreeIT extends SpringIvrIntegrationTest {

    @Autowired
    AdherenceCaptureTree adherenceCaptureTree;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllPatients allPatients;
    @Autowired
    WHPAdherenceService adherenceService;
    @Autowired
    AllAdherenceLogs allAdherenceLogs;
    @Autowired
    WhpIvrMessage whpIvrMessage;

    Provider provider;

    Patient patient1;
    Patient patient2;
    Patient patient3;

    private ReportingPublisherService reportingPublisherService;

    @Before
    public void setup() throws IOException, InterruptedException {
        super.setup();
        adherenceCaptureTree.load();

        LocalDate lastMonday = currentAdherenceCaptureWeek().startDate();
        adjustDateTime(lastMonday);

        reportingPublisherService = (ReportingPublisherService) getBean("reportingPublisherService");
        setUpTestData();
    }

    private void setUpTestData() {
        provider = new ProviderBuilder().withDefaults().build();
        String providerId = provider.getProviderId();
        allProviders.add(provider);

        LocalDate treatmentStartDate = DateUtil.today().minusDays(10);
        patient1 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId("patientid1").withProviderId(providerId).build();
        patient2 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId("patientid2").withProviderId(providerId).build();
        patient3 = new PatientBuilder().withDefaults().withTherapyStartDate(treatmentStartDate).withPatientId("patientid3").withProviderId(providerId).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
    }

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
                        alphaNumeric(id("patientid1")),
                        wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldPlayWindowClosedMessage_uponCallBeginning_outsideAdherenceCaptureWindow() throws IOException {
        LocalDate lastMonday = currentAdherenceCaptureWeek().startDate();
        adjustDateTime(lastMonday.plusDays(3));
        KooKooIvrResponse ivrResponse = startCall(provider.getPrimaryMobile());
        assertThat(ivrResponse.getPlayAudio(),
                is(audioList(
                        wav(WINDOW_OVER),
                        wav(THANK_YOU))));
    }

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
        sendDtmf("2");

        KooKooIvrResponse ivrResponse = sendDtmf("1");
        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "2"),
                alphaNumeric(id("patientid2")),
                wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains(base64("/2/1")));
        assertThat(adherenceService.currentWeekAdherence(patient1).getDosesTaken(), is(2));
        verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
    }

    @Test
    public void shouldPublishAdherenceCaptureLog_uponEnteringValidAdherenceValueAndConfirmation() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("2");
        sendDtmf("1");
        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();
        assertThat(adherenceCaptureRequest.getStatus(), is(ADHERENCE_PROVIDED));
    }

    @Test
    public void shouldSkipSavingAdherence_uponSkippingCurrentPatient_ByPressing9() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("9");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "2"),
                alphaNumeric(id("patientid2")),
                wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/9"))));
    }

    @Test
    public void shouldPublishSkipPatientLog_uponSkippingCurrentPatient_byPressing9() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("9");
        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();
        assertThat(adherenceCaptureRequest.getStatus(), is(SKIPPED));
    }

    @Test
    public void shouldRepeatAdherencePrompts_ForInvalidAdherenceInput_withinReplayThreshold() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("*");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(INVALID_ADHERENCE_MESSAGE_PART1, TREATMENT_CATEGORY_GOVT, INVALID_ADHERENCE_MESSAGE_PART2, "0", INVALID_ADHERENCE_MESSAGE_PART3, "3", INVALID_ADHERENCE_MESSAGE_PART4),
                wav(PATIENT_LIST, "1"), alphaNumeric(id("patientid1")), wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/*"))));
    }

    @Test
    public void shouldPublishInvalidAdherenceValueLog_ForEachInvalidAdherenceInput() {
        startCall(provider.getPrimaryMobile());

        sendDtmf("*");

        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();

        assertThat(adherenceCaptureRequest.getStatus(), is(INVALID));
    }

    @Test
    public void shouldBeginAdherenceCaptureForNextPatient_ForInvalidAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("*");
        sendDtmf("*");
        sendDtmf("*");
        KooKooIvrResponse ivrResponse = sendDtmf("*");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id("patientid2")), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldNotPublishSkipPatientLog_ForInvalidAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("*");
        sendDtmf("*");
        sendDtmf("*");
        sendDtmf("*");
        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService, times(4)).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();

        assertThat(adherenceCaptureRequest.getStatus(), is(INVALID));
    }

    @Test
    public void shouldRepeatAdherencePrompts_ForNoAdherenceInput_withinReplayThreshold() {
        startCall(provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(PATIENT_LIST, "1"), alphaNumeric(id("patientid1")), wav(ENTER_ADHERENCE))));

        assertTrue(ivrResponse.getGotoUrl().contains((base64("/"))));
    }

    @Test
    public void shouldPublishNoAdherenceValueLog_ForEachInvalidAdherenceInput() {
        startCall(provider.getPrimaryMobile());

        sendDtmf("");

        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();

        assertThat(adherenceCaptureRequest.getStatus(), is(NO_INPUT));
    }

    @Test
    public void shouldBeginAdherenceCaptureForNextPatient_ForNoAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("");
        sendDtmf("");
        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id("patientid2")), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldNotPublishSkipPatientLog_ForNoAdherenceInput_exceedingReplayThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("");
        sendDtmf("");
        sendDtmf("");
        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService, times(3)).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();

        assertThat(adherenceCaptureRequest.getStatus(), is(NO_INPUT));
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
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id("patientid2")), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldSkipCurrentPatientAndProceedToCaptureNextPatientsAdherence_ForNoAdherenceConfirmInput_exceedingMenuRepeatThreshold() {
        startCall(provider.getPrimaryMobile());
        sendDtmf("3");
        sendDtmf("");
        sendDtmf("");
        KooKooIvrResponse ivrResponse = sendDtmf("");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(MENU_REPEAT_FAILURE), wav(PATIENT_LIST, "2"), alphaNumeric(id("patientid2")), wav(ENTER_ADHERENCE))));
    }

    @Test
    public void shouldRecordAdherenceForMultiplePatients() {
        startCall(provider.getPrimaryMobile());
        recordAdherence("2");
        recordAdherence("3");
        KooKooIvrResponse ivrResponse = recordAdherence("2");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav(THANK_YOU, END_OF_CALL_ADHERENCE_PROVIDED_FOR, "3", END_OF_CALL_ADHERENCE_OUT_OF, "3", END_OF_CALL_ADHERENCE_TOTAL_PATIENTS, COMPLETION_MESSAGE, MUSIC_END_NOTE))));

        assertNull(ivrResponse.getGotoUrl());
        assertThat(ivrResponse.callEnded(), is(true));
    }

    @Test
    public void shouldPublishCallLogForValidAdherenceCapture_OnDisconnect() {
        startCall(provider.getPrimaryMobile());
        recordAdherence("2");
        recordAdherence("3");
        recordAdherence("2");
        disconnectCall();

        ArgumentCaptor<AdherenceCallLogRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCallLogRequest.class);
        verify(reportingPublisherService).reportCallLog(argumentCaptor.capture());
        AdherenceCallLogRequest callLogRequest = argumentCaptor.getValue();

        assertThat(callLogRequest.getProviderId(), is(provider.getProviderId()));
        assertThat(callLogRequest.getTotalPatients(), is(3));
        assertThat(callLogRequest.getAdherenceCaptured(), is(3));
        assertThat(callLogRequest.getAdherenceNotCaptured(), is(0));
        assertThat(callLogRequest.getFlashingCallId(), is(flashingCallId));
    }

    @Test
    public void shouldPlayAdherenceSummaryWhenProviderHasProvidedAdherenceForAllPatients() {
        startCall(provider.getPrimaryMobile());
        recordAdherence("2");
        recordAdherence("3");
        recordAdherence("2");

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

    @After
    public void tearDown() {
        allPatients.removeAll();
        allProviders.removeAll();
        allAdherenceLogs.removeAll();
        reset(reportingPublisherService);
    }

    private KooKooIvrResponse recordAdherence(String adherence) {
        sendDtmf(adherence);
        return sendDtmf("1");
    }

}