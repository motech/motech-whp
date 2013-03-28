package org.motechproject.whp.patientivralert.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PatientAdherenceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String mobileNumber;
    private int missingWeeks;

    public PatientAdherenceRecord() {
    }

    public PatientAdherenceRecord(String patientId, String mobileNumber, int missingWeeks) {
        this.patientId = patientId;
        this.mobileNumber = mobileNumber;
        this.missingWeeks = missingWeeks;
    }
}