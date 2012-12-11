package org.motechproject.whp.adherenceapi.reporting;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static java.lang.Long.parseLong;
import static org.apache.commons.lang.StringUtils.isBlank;

public class AdherenceCaptureRequestBuilder {

    private AdherenceCaptureRequest request;

    public final static String VALID = "Valid";
    public final static String INVALID = "Invalid";

    public AdherenceCaptureRequestBuilder() {
    }

    public static AdherenceCaptureRequestBuilder adherenceCaptureRequest() {
        return new AdherenceCaptureRequestBuilder();
    }

    public AdherenceCaptureRequest validAdherence(AdherenceValidationRequest request, ProviderId providerId) {
        initRequest(request, providerId);
        this.validInput(request.getDoseTakenCount());
        return this.request;
    }

    public AdherenceCaptureRequest invalidAdherence(AdherenceValidationRequest request, ProviderId providerId) {
        initRequest(request, providerId);
        this.invalidInput(request.getDoseTakenCount());
        return this.request;
    }

    private AdherenceCaptureRequestBuilder validInput(String input) {
        request.setValid(true);
        request.setStatus(VALID);
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureRequestBuilder invalidInput(String input) {
        request.setValid(false);
        request.setStatus(INVALID);
        request.setSubmittedValue(input);
        return this;
    }

    private void initRequest(AdherenceValidationRequest request, ProviderId providerId) {
        this.request = new AdherenceCaptureRequest();
        this.request.setChannelId("IVR");
        this.byProvider(providerId.value()).forRequest(request);
    }

    private AdherenceCaptureRequestBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    private AdherenceCaptureRequestBuilder forRequest(AdherenceValidationRequest request) {
        this.forPatient(request.getPatientId())
                .throughMobile(request.getMsisdn())
                .onCall(request.getCallId())
                .withTimeTaken(request.getTimeTaken());
        return this;
    }

    private AdherenceCaptureRequestBuilder withTimeTaken(String timeTaken) {
        if (isBlank(timeTaken)) {
            request.setTimeTaken(0l);
        } else {
            request.setTimeTaken(parseLong(timeTaken));
        }
        return this;
    }

    private AdherenceCaptureRequestBuilder byProvider(String providerId) {
        request.setProviderId(providerId);
        return this;
    }

    private AdherenceCaptureRequestBuilder throughMobile(String mobileNumber) {
        request.setSubmittedBy(mobileNumber);
        return this;
    }

    private AdherenceCaptureRequestBuilder onCall(String callId) {
        request.setCallId(callId);
        return this;
    }
}
