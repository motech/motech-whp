package org.motechproject.whp.adherenceapi.reporting;


import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.adherenceapi.domain.IVRInput;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static java.lang.Long.parseLong;

public class AdherenceCaptureReportRequest {

    private AdherenceValidationRequest request;
    private ProviderId providerId;
    private boolean valid;

    public AdherenceCaptureReportRequest(AdherenceValidationRequest request, ProviderId providerId, boolean valid) {
        this.request = request;
        this.providerId = providerId;
        this.valid = valid;
    }

    public AdherenceCaptureReportRequest(AdherenceValidationRequest request, ProviderId providerId) {
        this(request, providerId, true);
    }

    public AdherenceCaptureReportRequest() {
        this(null, null, false);
    }

    public AdherenceCaptureRequest captureRequest() {
        AdherenceCaptureRequest result = new AdherenceCaptureRequest();
        result.setChannelId(withChannel().getChannelId());
        result.setTimeTaken(withTimeTaken(request.getTimeTaken()).getTimeTaken());
        result.setStatus(withStatus().getStatus());
        result.setValid(withValidity().isValid());

        result.setCallId(request.getCallId());
        result.setProviderId(providerId.value());
        result.setSubmittedBy(providerId.value());
        result.setPatientId(request.getPatientId());
        result.setSubmittedValue(request.getDoseTakenCount());
        return result;
    }

    protected AdherenceCaptureRequest withChannel() {
        AdherenceCaptureRequest result = new AdherenceCaptureRequest();
        result.setChannelId("IVR");
        return result;
    }

    protected AdherenceCaptureRequest withTimeTaken(String timeTaken) {
        AdherenceCaptureRequest result = new AdherenceCaptureRequest();
        if (StringUtils.isNotBlank(timeTaken)) {
            result.setTimeTaken(parseLong(timeTaken));
        } else {
            result.setTimeTaken(0l);
        }
        return result;
    }

    protected AdherenceCaptureRequest withValidity() {
        AdherenceCaptureRequest result = new AdherenceCaptureRequest();
        result.setValid(valid);
        return result;
    }

    protected AdherenceCaptureRequest withStatus() {
        AdherenceCaptureRequest result = new AdherenceCaptureRequest();
        result.setStatus((valid) ? IVRInput.VALID.name() : IVRInput.INVALID.name());
        return result;
    }
}
