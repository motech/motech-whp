package org.motechproject.whp.adherenceapi.reporting;

import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.apache.commons.lang.StringUtils.isBlank;

public class AdherenceCaptureReportRequest {

    private AdherenceValidationRequest request;
    private ProviderId providerId;
    private boolean valid;
    private AdherenceCaptureStatus status;

    public AdherenceCaptureReportRequest(AdherenceValidationRequest request, ProviderId providerId, boolean valid, AdherenceCaptureStatus status) {
        this.request = request;
        this.providerId = providerId;
        this.valid = valid;
        this.status = status;
    }

    public AdherenceCaptureRequest request() {
        AdherenceCaptureRequest captureRequest = new AdherenceCaptureRequest();
        withChannelId(captureRequest);
        withCallId(captureRequest);
        withTimeTaken(captureRequest);
        withIvrFileLength(captureRequest);
        withSubmittedValue(captureRequest);
        withProviderId(captureRequest);
        withValidity(captureRequest);
        withPatient(captureRequest);
        return captureRequest;
    }

    private void withIvrFileLength(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setIvrFileLength(Long.parseLong(request.getIvrFileLength()));
    }

    private AdherenceCaptureRequest withChannelId(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setChannelId("IVR");
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withCallId(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setCallId(request.getCallId());
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withTimeTaken(AdherenceCaptureRequest adherenceCaptureRequest) {
        if (isBlank(request.getTimeTaken())) {
            adherenceCaptureRequest.setTimeTaken(0l);
        } else {
            adherenceCaptureRequest.setTimeTaken(Long.parseLong(request.getTimeTaken()));
        }
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withSubmittedValue(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setSubmittedValue(request.getDoseTakenCount());
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withProviderId(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setProviderId(providerId.value());
        adherenceCaptureRequest.setSubmittedBy(providerId.value());
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withPatient(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setPatientId(request.getPatientId());
        return adherenceCaptureRequest;
    }

    private AdherenceCaptureRequest withValidity(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setValid(valid);
        return withStatus(adherenceCaptureRequest);
    }

    private AdherenceCaptureRequest withStatus(AdherenceCaptureRequest adherenceCaptureRequest) {
        adherenceCaptureRequest.setStatus(status.getValue());
        return adherenceCaptureRequest;
    }
}
