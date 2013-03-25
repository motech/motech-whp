package org.motechproject.whp.patientivralert.model;

import lombok.Data;

@Data
public class PatientAdherenceRecord {
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