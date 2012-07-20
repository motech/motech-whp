package org.motechproject.whp.reporting.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class AdherenceCaptureRequest implements Serializable {
    private String patientId;
    private String providerId;
    private int adherence;

    public AdherenceCaptureRequest(String providerId, String patientId, int adherence) {
        this.providerId = providerId;
        this.patientId = patientId;
        this.adherence = adherence;
    }
}

