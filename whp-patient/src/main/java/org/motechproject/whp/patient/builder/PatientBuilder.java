package org.motechproject.whp.patient.builder;

import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.*;

import java.util.Arrays;
import java.util.List;

public class PatientBuilder {

    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    private final Patient patient;

    private PatientBuilder() {
        patient = new Patient();
    }

    public PatientBuilder withDefaults() {
        ProvidedTreatment currentProvidedTreatment = new ProvidedTreatment();
        Treatment treatment = new Treatment();
        treatment.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, threeDaysAWeek));
        currentProvidedTreatment.setTreatment(treatment);
        patient.setPatientId("patientId");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setGender(Gender.O);
        patient.setPatientType(PatientType.New);
        patient.setPhoneNumber("1234567890");
        patient.setCurrentProvidedTreatment(currentProvidedTreatment);
        return this;
    }

    public static PatientBuilder startRecording() {
        return new PatientBuilder();
    }

    public Patient build() {
        return patient;
    }
}
