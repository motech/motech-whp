package org.motechproject.whp.ivr.builder.request;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.session.IvrSession;
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
                .throughMobile(ivrSession.mobileNumber())
                .onCall(ivrSession.callId())
                .withStartOfAdherenceSubmission(ivrSession.startOfAdherenceSubmission())
                .build();
        return this;
    }

    public AdherenceCaptureRequest build() {
        return request;
    }

    private AdherenceCaptureBuilder withStartOfAdherenceSubmission(DateTime time) {
        DateTime now = DateUtil.now();
        Period period = new Period(time, now);
        request.setTimeTaken(new Long(period.getSeconds()));
        return this;
    }

    private AdherenceCaptureBuilder withInput(IVRInput userInput) {
        request.setStatus(userInput.status());
        request.setSubmittedValue(userInput.input());
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
