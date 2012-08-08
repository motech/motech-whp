package org.motechproject.whp.ivr.builder.request;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

public class AdherenceCaptureBuilder {

    private AdherenceCaptureRequest request;
    private final static String UNKNOWN = "Unknown";
    private final static String SKIPPED = "Skipped";
    private final static String TAKEN = "Taken";

    public AdherenceCaptureBuilder() {

    }

    public static AdherenceCaptureBuilder adherenceCapture() {
        return new AdherenceCaptureBuilder();
    }

    public AdherenceCaptureRequest validAdherence(String patientId, IvrSession ivrSession) {
        initRequest(patientId, ivrSession);
        this.validInput(ivrSession.adherenceInputForCurrentPatient().input());
        return request;
    }

    public AdherenceCaptureRequest skipAdherence(String patientId, IvrSession ivrSession) {
        initRequest(patientId, ivrSession);
        this.skipInput();
        return request;
    }

    public AdherenceCaptureRequest invalidAdherence(String patientId, IvrSession ivrSession, String input) {
        initRequest(patientId, ivrSession);
        this.invalidInput(Integer.parseInt(input));
        return request;
    }

    private AdherenceCaptureBuilder validInput(Integer input) {
        request.setValid(true);
        request.setStatus(TAKEN);
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureBuilder invalidInput(Integer input) {
       request.setValid(false);
       request.setStatus(UNKNOWN);
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureBuilder skipInput() {
        request.setValid(true);
        request.setStatus(SKIPPED);
        request.setSubmittedValue(Integer.valueOf(IVRInput.SKIP_PATIENT_CODE));
        return this;
    }

    private void initRequest(String patientId, IvrSession ivrSession) {
        request = new AdherenceCaptureRequest();
        request.setChannelId("IVR");
        this.forPatient(patientId).forSession(ivrSession);

    }

    private AdherenceCaptureBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    private AdherenceCaptureBuilder forSession(IvrSession ivrSession) {
        this.byProvider(ivrSession.providerId())
                .throughMobile(ivrSession.mobileNumber())
                .onCall(ivrSession.callId())
                .withStartOfAdherenceSubmission(ivrSession.startOfAdherenceSubmission());
        return this;
    }

    private AdherenceCaptureBuilder withStartOfAdherenceSubmission(DateTime time) {
        DateTime now = DateUtil.now();
        Period period = new Period(time, now);
        request.setTimeTaken(new Long(period.getSeconds()));
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
