package org.motechproject.whp.patient.domain;

public enum PatientType {

    New, PHSTransfer;

    public static PatientType get(String patientType) {
        if(New.name().toLowerCase().equals(patientType.toLowerCase())) return New;
        if(PHSTransfer.name().toLowerCase().equals(patientType.toLowerCase())) return PHSTransfer;
        return null;
    }

}
