package org.motechproject.whp.patient.builder;

import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientType;

public class PatientBuilder {

    private final Patient patient;

    private PatientBuilder() {
        patient = new Patient();
    }

    public PatientBuilder withDefaults() {
        patient.setPatientId("patientId");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setGender(Gender.O);
        patient.setPatientType(PatientType.New);
        patient.setPhoneNumber("1234567890");
        return this;
    }

    public static PatientBuilder startRecording() {
        return new PatientBuilder();
    }

    public Patient build() {
        return patient;
    }
}
