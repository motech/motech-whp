package org.motechproject.whp.ivr.tree;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.adherence.repository.AllAdherenceLogs;
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
import org.motechproject.whp.reports.contract.CallLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
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
        String providerId = "provider1";
        provider = new Provider(providerId, "123456", "Vaishali", DateUtil.now());
        allProviders.add(provider);

        LocalDate treatmentStartDate = DateUtil.today().minusDays(10);
        patient1 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid1").withProviderId(providerId).build();
        patient2 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid2").withProviderId(providerId).build();
        patient3 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid3").withProviderId(providerId).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
    }

    @Test
    public void shouldPlayWelcomeMessage() {
        String sessionId = UUID.randomUUID().toString();
        KooKooIvrResponse ivrResponse = startCall(sessionId, provider.getPrimaryMobile());
        List<String> playAudioList = ivrResponse.getPlayAudio();
        assertThat(playAudioList, audioList(wav("musicEnter", "welcomeMessage")));
    }

    @Test
    public void shouldPlayAdherenceSummary() {
        String sessionId = UUID.randomUUID().toString();
        KooKooIvrResponse ivrResponse = startCall(sessionId, provider.getPrimaryMobile());
        assertThat(ivrResponse.getPlayAudio(),
                is(audioList(
                        wav("instructionalMessage1"),
                        alphaNumeric("0"),
                        wav("instructionalMessage2"),
                        alphaNumeric("3"),
                        wav("instructionalMessage3"),
                        wav("patientList"),
                        alphaNumeric("1"),
                        alphaNumeric(id("patientid1")),
                        wav("enterAdherence"))));
    }

    @Test
    public void shouldAskForConfirmation_UponEnteringValidAdherenceValue() {
        String sessionId = UUID.randomUUID().toString();
        startCall(sessionId, provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf(sessionId, "2");

        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertThat(adherenceSummaryPatient1.getDosesTaken(), is(0));

        assertThat(ivrResponse.getPlayAudio(),
                is(audioList(
                        wav("confirmMessage1"),
                        alphaNumeric(id(patient1.getPatientId())),
                        wav("confirmMessage1a", "3", "confirmMessage2", "2", "confirmMessage3", "confirmMessage4"))));

        assertThat(ivrResponse.getGotoUrl(), is("http://localhost:7080/whp/kookoo/ivr?type=kookoo&ln=en&tree=adherenceCapture&trP=" + base64("/2")));
    }

    @Test
    public void shouldRecordAdherenceForPatient() {
        String sessionId = UUID.randomUUID().toString();
        startCall(sessionId, provider.getPrimaryMobile());
        sendDtmf(sessionId, "2");

        KooKooIvrResponse ivrResponse = sendDtmf(sessionId, "1");
        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav("patientList", "2"),
                wav(id("patientid2")),
                wav("enterAdherence"))));

        assertThat(ivrResponse.getGotoUrl(), is(contains(base64("/2/1"))));
        assertThat(adherenceService.currentWeekAdherence(patient1).getDosesTaken(), is(2));
        verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
    }

    @Test
    public void shouldSkipSavingAdherence_OnPressing9() {
        String sessionId = UUID.randomUUID().toString();
        startCall(sessionId, provider.getPrimaryMobile());

        KooKooIvrResponse ivrResponse = sendDtmf(sessionId, "9");

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav("patientList", "2"),
                wav(id("patientid2")),
                wav("enterAdherence"))));

        assertThat(ivrResponse.getGotoUrl(), is(contains(base64("/9"))));
        verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
    }

    @Test
    public void shouldRecordAdherenceForMultiplePatients() {
        String sessionId = UUID.randomUUID().toString();
        startCall(sessionId, provider.getPrimaryMobile());

        String adherenceCapturedForFirstPatient = "2";
        recordAdherence(sessionId, adherenceCapturedForFirstPatient);

        String adherenceCapturedForSecondPatient = "3";
        recordAdherence(sessionId, adherenceCapturedForSecondPatient);

        String adherenceCapturedForThirdPatient = "2";
        sendDtmf(sessionId, adherenceCapturedForThirdPatient);
        KooKooIvrResponse ivrResponse = recordAdherence(sessionId, adherenceCapturedForThirdPatient);

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav("thankYou", "summaryMessage1", "3", "summaryMessage2", "3", "summaryMessage3", "completionMessage", "musicEnd-note"))));

        assertThat(ivrResponse.getGotoUrl(), is(contains(base64("/9"))));
        assertThat(ivrResponse.callEnded(), is(true));

        // TODO : should be a separate test
        ArgumentCaptor<CallLogRequest> argumentCaptor = ArgumentCaptor.forClass(CallLogRequest.class);
        verify(reportingPublisherService).reportCallLog(argumentCaptor.capture());
        CallLogRequest callLogRequest = argumentCaptor.getValue();

        assertThat(callLogRequest.getProviderId(), is(provider.getProviderId()));
        assertThat(callLogRequest.getTotalPatients(), is(3));
        assertThat(callLogRequest.getAdherenceCaptured(), is(3));
        assertThat(callLogRequest.getAdherenceNotCaptured(), is(0));
    }

    @Test
    public void shouldPlayAdherenceSummaryWhenProviderHasProvidedAdherenceForAllPatients() {
        String sessionId1 = UUID.randomUUID().toString();
        startCall(sessionId1, provider.getPrimaryMobile());

        String adherenceCapturedForFirstPatient = "2";
        recordAdherence(sessionId1, adherenceCapturedForFirstPatient);

        String adherenceCapturedForSecondPatient = "3";
        recordAdherence(sessionId1, adherenceCapturedForSecondPatient);

        String adherenceCapturedForThirdPatient = "2";
        sendDtmf(sessionId1, adherenceCapturedForThirdPatient);
        recordAdherence(sessionId1, adherenceCapturedForThirdPatient);

        String sessionId2 = UUID.randomUUID().toString();
        KooKooIvrResponse ivrResponse = startCall(sessionId2, provider.getPrimaryMobile());

        assertThat(ivrResponse.getPlayAudio(), is(audioList(
                wav("summaryMessage1", "3", "summaryMessage2", "3", "summaryMessage3", "completionMessage", "musicEnd-note"))));
        assertThat(ivrResponse.callEnded(), is(true));
    }

    @Test
    public void shouldPlayWindowClosedPrompt_IfNotInAdherenceCaptureWindow() throws IOException {
        String sessionId = UUID.randomUUID().toString();
        LocalDate thursday = currentAdherenceCaptureWeek().startDate().plusDays(3);
        adjustDateTime(thursday);

        KooKooIvrResponse ivrResponse = startCall(sessionId, provider.getPrimaryMobile());

        assertThat(ivrResponse.getPlayAudio(), is(audioList(wav("windowOverMessage", "thankYou"))));
        assertThat(ivrResponse.callEnded(), is(true));
    }

    @After
    public void tearDown() {
        allPatients.remove(allPatients.findByPatientId(patient1.getPatientId()));
        allPatients.remove(allPatients.findByPatientId(patient2.getPatientId()));
        allPatients.remove(allPatients.findByPatientId(patient3.getPatientId()));
        allProviders.remove(allProviders.findByMobileNumber("123456"));
        allAdherenceLogs.removeAll();
        reset(reportingPublisherService);
    }

    private KooKooIvrResponse recordAdherence(String sessionId, String adherenceCapturedForFirstPatient) {
        sendDtmf(sessionId, adherenceCapturedForFirstPatient);
        return sendDtmf(sessionId, "1");
    }

}