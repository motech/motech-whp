package org.motechproject.whp.ivr.builder.request;

import org.joda.time.DateTime;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

public class AdherenceCaptureRequestBuilder {

    private AdherenceCaptureRequest request;

    public final static String INVALID = "Invalid";
    public final static String NO_INPUT = "NoInput";
    public final static String SKIPPED = "Skipped";
    public final static String ADHERENCE_PROVIDED = "Given";

    public AdherenceCaptureRequestBuilder() {
    }

    public static AdherenceCaptureRequestBuilder adherenceCaptureRequest() {
        return new AdherenceCaptureRequestBuilder();
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
        this.invalidInput(input);
        return request;
    }

    public AdherenceCaptureRequest noAdherenceInput(String patientId, IvrSession ivrSession) {
        initRequest(patientId, ivrSession);
        this.noInput();
        return request;
    }

    private AdherenceCaptureRequestBuilder validInput(String input) {
        request.setValid(true);
        request.setStatus(ADHERENCE_PROVIDED);
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureRequestBuilder invalidInput(String input) {
        request.setValid(false);
        request.setStatus(INVALID);
        request.setSubmittedValue(input);
        return this;
    }

    private AdherenceCaptureRequestBuilder noInput() {
        request.setValid(false);
        request.setStatus(NO_INPUT);
        request.setSubmittedValue(IVRInput.NO_INPUT_CODE);
        return this;
    }

    private AdherenceCaptureRequestBuilder skipInput() {
        request.setValid(true);
        request.setStatus(SKIPPED);
        request.setSubmittedValue(IVRInput.SKIP_PATIENT_CODE);
        return this;
    }

    private void initRequest(String patientId, IvrSession ivrSession) {
        request = new AdherenceCaptureRequest();
        request.setChannelId("IVR");
        this.forPatient(patientId).forSession(ivrSession);
    }

    private AdherenceCaptureRequestBuilder forPatient(String patientId) {
        request.setPatientId(patientId);
        return this;
    }

    private AdherenceCaptureRequestBuilder forSession(IvrSession ivrSession) {
        this.byProvider(ivrSession.providerId())
                .throughMobile(ivrSession.phoneNumber())
                .onCall(ivrSession.callId())
                .withStartOfAdherenceSubmission(ivrSession.startOfAdherenceSubmission());
        return this;
    }

    private AdherenceCaptureRequestBuilder withStartOfAdherenceSubmission(DateTime time) {
        DateTime now = DateUtil.now();
        request.setTimeTaken(now.minus(time.getMillis()).getMillis());
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
