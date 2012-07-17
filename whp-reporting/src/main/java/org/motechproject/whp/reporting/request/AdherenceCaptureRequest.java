package org.motechproject.whp.reporting.request;


public class AdherenceCaptureRequest {
    private String patientId;
    private String providerId;
    private int adherence;

    public AdherenceCaptureRequest(String providerId, String patientId, int adherence) {
        this.providerId = providerId;
        this.patientId = patientId;
        this.adherence = adherence;
    }


    public String getPatientId() {
        return patientId;
    }

    public String getProviderId() {
        return providerId;
    }
}

