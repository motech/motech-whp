package org.motechproject.whp.ivr.tree;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.reports.contract.AdherenceCallLogRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureRequestBuilder.*;

public class ReportingITPart extends IvrCallFlowITPart {
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
    public void shouldPublishAdherenceCaptureLog_uponEnteringValidAdherenceValueAndConfirmation() {
        startCall(provider.getPrimaryMobile());
        recordConfirmedAdherence("2");
        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();
        assertThat(adherenceCaptureRequest.getStatus(), is(ADHERENCE_PROVIDED));
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
    public void shouldPublishNoAdherenceValueLog_ForEachInvalidAdherenceInput() {
        startCall(provider.getPrimaryMobile());

        sendDtmf("");

        ArgumentCaptor<AdherenceCaptureRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCaptureRequest.class);
        verify(reportingPublisherService).reportAdherenceCapture(argumentCaptor.capture());
        AdherenceCaptureRequest adherenceCaptureRequest = argumentCaptor.getValue();

        assertThat(adherenceCaptureRequest.getStatus(), is(NO_INPUT));
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
    public void shouldPublishCallLogUponAdherenceCapture() {
        startCall(provider.getPrimaryMobile());
        recordConfirmedAdherence("2");
        recordConfirmedAdherence("3");
        recordConfirmedAdherence("2");

        ArgumentCaptor<AdherenceCallLogRequest> argumentCaptor = ArgumentCaptor.forClass(AdherenceCallLogRequest.class);
        verify(reportingPublisherService).reportCallLog(argumentCaptor.capture());
        AdherenceCallLogRequest callLogRequest = argumentCaptor.getValue();

        assertThat(callLogRequest.getProviderId(), is(provider.getProviderId()));
        assertThat(callLogRequest.getTotalPatients(), is(3));
        assertThat(callLogRequest.getAdherenceCaptured(), is(3));
        assertThat(callLogRequest.getAdherenceNotCaptured(), is(0));
    }
}
