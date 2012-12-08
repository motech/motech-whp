package org.motechproject.whp.adherenceapi.service;

import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.user.service.ProviderService;

import static java.lang.Long.parseLong;
import static org.motechproject.whp.adherenceapi.domain.IVRInput.INVALID;

public class AdherenceCaptureRequestBuilder {

    private AdherenceCaptureRequest request;
    private ProviderService providerService;

    public AdherenceCaptureRequestBuilder(ProviderService providerService) {
        this.providerService = providerService;
    }

    public AdherenceCaptureRequest forInvalidCase(AdherenceValidationRequest validationRequest) {
        initRequest(validationRequest);
        this.invalidInput(validationRequest.getDoseTakenCount());
        return request;
    }

    private void initRequest(AdherenceValidationRequest validationRequest) {
        request = new AdherenceCaptureRequest();
        request.setChannelId(ChannelId.IVR.name());
        this.forPatient(validationRequest.getPatientId()).forValidationRequest(validationRequest);
    }

    private AdherenceCaptureRequestBuilder invalidInput(String input) {
        request.setValid(false);
        request.setStatus(INVALID.getText());
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureRequestBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    private AdherenceCaptureRequestBuilder forValidationRequest(AdherenceValidationRequest validationRequest) {
        this.byProvider(providerService.findByMobileNumber(validationRequest.getMsisdn()).getProviderId())
                .throughMobile(validationRequest.getMsisdn())
                .onCall(validationRequest.getCallId())
                .withTimeTaken(validationRequest.getTimeTaken());
        return this;
    }

    private AdherenceCaptureRequestBuilder withTimeTaken(String timeTaken) {
        request.setTimeTaken(parseLong(timeTaken));
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
