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
        patient.setPhoneNumber("1234567890");
        patient.setCurrentTreatment(defaultTreatment());
        return this;
    }

    public Patient build() {
        return patient;
    }

    private Treatment defaultTreatment() {
        LocalDate today = DateUtil.today();
        Treatment treatment = new Treatment();
        treatment.setTbId("elevenDigit");
        treatment.setTherapy(defaultTherapy());
        treatment.setPatientAddress(defaultAddress());
        treatment.setPatientType(PatientType.New);
        treatment.addSmearTestResult(new SmearTestRecord(SmearTestSampleInstance.PreTreatment, today, SmearTestResult.Negative, today, SmearTestResult.Negative));
        treatment.addWeightStatistics(new WeightStatisticsRecord(WeightInstance.PreTreatment, 100.0, today));
        return treatment;
    }

    private Therapy defaultTherapy() {
        Therapy therapy = new Therapy();
        therapy.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, 24, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClass.P);
        return therapy;
    }

    private Address defaultAddress() {
        return new Address("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public PatientBuilder withType(PatientType type) {
        patient.getCurrentTreatment().setPatientType(type);
        return this;
    }

    public PatientBuilder withPatientId(String patientId) {
        patient.setPatientId(patientId);
        return this;
    }

    public PatientBuilder onTreatmentFrom(LocalDate date) {
        patient.latestTreatment().setStartDate(date);
        return this;
    }

    public PatientBuilder withTbId(String tbId) {
        patient.getCurrentTreatment().setTbId(tbId);
        return this;
    }

    public PatientBuilder withCurrentTreatment(Treatment currentTreatment) {
        patient.setCurrentTreatment(currentTreatment);
        return this;
    }

    public PatientBuilder withStatus(PatientStatus status) {
        patient.setStatus(status);
        return this;
    }

    public PatientBuilder withMigrated(boolean migrated) {
        patient.setMigrated(migrated);
        return this;
    }
}
