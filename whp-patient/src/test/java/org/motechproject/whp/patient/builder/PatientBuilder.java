package org.motechproject.whp.patient.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;

import java.util.Arrays;
import java.util.List;

public class PatientBuilder {

    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    private final Patient patient;

    public PatientBuilder() {
        patient = new Patient();
    }

    public PatientBuilder withDefaults() {
        patient.setPatientId("patientId");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setGender(Gender.O);
        patient.setPatientType(PatientType.New);
        patient.setPhoneNumber("1234567890");
        patient.setCurrentProvidedTreatment(defaultProvidedTreatment());
        return this;
    }

    public Patient build() {
        return patient;
    }

    private ProvidedTreatment defaultProvidedTreatment() {
        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setTbId("tbId");
        providedTreatment.setTreatment(defaultTreatment());
        providedTreatment.setPatientAddress(defaultAddress());
        return providedTreatment;
    }

    private Treatment defaultTreatment() {
        Treatment treatment = new Treatment();
        LocalDate today = DateUtil.today();
        treatment.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, threeDaysAWeek));
        treatment.addSmearTestResult(new SmearTestResults(SmearTestSampleInstance.PreTreatment, today, SmearTestResult.Negative, today, SmearTestResult.Negative));
        treatment.addWeightStatistics(new WeightStatistics(WeightInstance.PreTreatment, 100.0, today));
        return treatment;
    }

    private Address defaultAddress() {
        return new Address("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public PatientBuilder withType(PatientType type) {
        patient.setPatientType(type);
        return this;
    }

    public PatientBuilder withPatientId(String patientId) {
        patient.setPatientId(patientId);
        return this;
    }

    public PatientBuilder onTreatmentFrom(LocalDate date) {
        patient.getCurrentProvidedTreatment().getTreatment().setDoseStartDate(date);
        return this;
    }

    public PatientBuilder withTbId(String tbId) {
        patient.getCurrentProvidedTreatment().setTbId(tbId);
        return this;
    }

    public PatientBuilder withCurrentProvidedTreatment(ProvidedTreatment currentProvidedTreatment) {
        patient.setCurrentProvidedTreatment(currentProvidedTreatment);
        return this;
    }

    public PatientBuilder withStatus(PatientStatus status) {
        patient.setStatus(status);
        return this;
    }
}
