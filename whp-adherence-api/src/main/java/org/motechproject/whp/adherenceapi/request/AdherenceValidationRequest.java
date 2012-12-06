package org.motechproject.whp.adherenceapi.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdherenceValidationRequest implements Serializable {
    private String patientId;
    private String doseTakenCount;
    private String msisdn;
    private String callId;
    private String timeTaken;
}
