package org.motechproject.whp.ivr.builder;

import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.reports.webservice.request.AdherenceCaptureRequest;

import static java.lang.Integer.parseInt;

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

    public AdherenceCaptureBuilder withInput(Integer userInput) {
        request.setStatus(PillStatus.getFromIVRInput(userInput).name().toUpperCase());
        request.setSubmittedValue(userInput);
        return this;
    }

    public AdherenceCaptureBuilder byProvider(String providerId) {
        request.setProviderId(providerId);
        return this;
    }

    public AdherenceCaptureBuilder throughMobile(String mobileNumber) {
        request.setSubmittedBy(mobileNumber);
        return this;
    }

    public AdherenceCaptureBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    public AdherenceCaptureBuilder onCall(String callId) {
        request.setCallId(callId);
        return this;
    }

    public AdherenceCaptureRequest build() {
        return request;
    }
}
