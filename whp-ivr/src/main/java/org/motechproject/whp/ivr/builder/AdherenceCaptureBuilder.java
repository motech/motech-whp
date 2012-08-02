package org.motechproject.whp.ivr.builder;

import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

public class AdherenceCaptureBuilder {

    private AdherenceCaptureRequest request;

    public AdherenceCaptureBuilder() {
        request = new AdherenceCaptureRequest();
        request.setValid(true);
        request.setChannelId("IVR");
    }

    public static AdherenceCaptureBuilder adherenceCapture() {
        return new AdherenceCaptureBuilder();
    }

    public AdherenceCaptureBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    public AdherenceCaptureBuilder forSession(IvrSession ivrSession) {
        request = this.withInput(ivrSession.adherenceInputForCurrentPatient())
                .byProvider(ivrSession.providerId())
                .throughMobile(ivrSession.getMobileNumber())
                .onCall(ivrSession.callId())
                .build();
        return this;
    }

    public AdherenceCaptureRequest build() {
        return request;
    }

    private AdherenceCaptureBuilder withInput(Integer userInput) {
        request.setStatus(PillStatus.getFromIVRInput(userInput).name().toUpperCase());
        request.setSubmittedValue(userInput);
        return this;
    }

    private AdherenceCaptureBuilder byProvider(String providerId) {
        request.setProviderId(providerId);
        return this;
    }

    private AdherenceCaptureBuilder throughMobile(String mobileNumber) {
        request.setSubmittedBy(mobileNumber);
        return this;
    }

    private AdherenceCaptureBuilder onCall(String callId) {
        request.setCallId(callId);
        return this;
    }
}
